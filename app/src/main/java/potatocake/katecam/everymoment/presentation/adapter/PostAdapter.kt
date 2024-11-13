package potatocake.katecam.everymoment.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.dto.response.getComments.Comment
import potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail.Post
import potatocake.katecam.everymoment.databinding.CommentItemBinding
import potatocake.katecam.everymoment.databinding.PostRecyclerHeaderBinding
import potatocake.katecam.everymoment.extensions.CustomDialog
import potatocake.katecam.everymoment.extensions.Like
import potatocake.katecam.everymoment.presentation.listener.OnDeleteCommentListener
import potatocake.katecam.everymoment.presentation.view.sub.PostFragment
import potatocake.katecam.everymoment.presentation.viewModel.PostViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostAdapter(
    private val parentFragment: PostFragment,
    private val listener: OnDeleteCommentListener,
    private val viewModel: PostViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var like: Like
    private var likeCnt = 0
    private var commentCnt = 0

    private var post: Post? = null
    private var images: List<String>? = null
    private var comments: List<Comment> = listOf()

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }

    fun updatePost(post: Post) {
        this.post = post
        notifyItemChanged(0)
    }

    fun updateImages(images: List<String>) {
        this.images = images
        notifyItemChanged(0)
    }

    fun updateComments(comments: List<Comment>) {
        this.comments = comments
        notifyDataSetChanged()
    }

    fun updateLikeCnt(likeCnt: Int) {
        this.likeCnt = likeCnt
        notifyItemChanged(0)
    }

    fun updateCommentCnt(commentCnt: Int) {
        this.commentCnt = commentCnt
        notifyItemChanged(0)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return VIEW_TYPE_HEADER
        else return VIEW_TYPE_ITEM
    }

    inner class ItemViewHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Comment) {
            binding.nickName.text = item.commentFriend.nickname
            binding.comment.text = item.content
            Glide.with(itemView.context)
                .load(item.commentFriend.profileImageUrl)
                .circleCrop()
                .into(binding.profileImg)
            binding.edit.isVisible = viewModel.checkIsUserId(item.commentFriend.id)
            setClickListeners()
        }

        private fun setClickListeners() {
            binding.root.setOnLongClickListener {
                val adjustedPosition = adapterPosition - 1
                val commentUserId = comments[adjustedPosition].commentFriend.id
                if (!viewModel.checkIsUserId(commentUserId)) return@setOnLongClickListener true
                val commentId = comments[adjustedPosition].id
                listener.onDeleteCommentRequested(commentId)
                true
            }

            binding.edit.setOnClickListener {
                binding.editComment.setText(binding.comment.text.toString())
                binding.comment.visibility = View.GONE
                binding.edit.visibility = View.GONE
                binding.editCommentView.visibility = View.VISIBLE
                binding.editTools.visibility = View.VISIBLE
            }

            binding.editComment.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) parentFragment.hideCommentWindow()
                else parentFragment.showCommentWindow()
            }

            binding.done.setOnClickListener {
                val newText = binding.editComment.text.toString()
                if (newText.isEmpty()) {
                    Toast.makeText(itemView.context, "빈 댓글은 작성할 수 없습니다", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val commentId = comments[adapterPosition - 1].id
                binding.comment.text = newText
                binding.comment.visibility = View.VISIBLE
                binding.edit.visibility = View.VISIBLE
                binding.editCommentView.visibility = View.GONE
                binding.editTools.visibility = View.GONE
                viewModel.patchComment(commentId, newText)
                parentFragment.showCommentWindow()
            }

            binding.cancel.setOnClickListener {
                binding.comment.visibility = View.VISIBLE
                binding.edit.visibility = View.VISIBLE
                binding.editCommentView.visibility = View.GONE
                binding.editTools.visibility = View.GONE
                parentFragment.showCommentWindow()
            }
        }
    }

    inner class HeaderViewHolder(private val binding: PostRecyclerHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            if (!::like.isInitialized) {
                like = Like(binding.like)
            }
            setDiaryContent()
            setImages()
            setCommentCnt()
            setLikeCnt()
            setClickListeners()
        }

        fun setCommentCnt() {
            binding.commentCnt.text = commentCnt.toString()
        }

        fun setLikeCnt() {
            binding.likeCnt.text = likeCnt.toString()
        }

        private fun setImages() {
            images?.let {
                if (it.isNotEmpty()) {
                    if (it.size == 2) {
                        binding.image2.visibility = View.VISIBLE
                        binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(itemView.context).load(it[1]).into(binding.image2)
                    }
                    binding.image1.visibility = View.VISIBLE
                    binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(itemView.context).load(it[0]).into(binding.image1)
                    binding.images.visibility = View.VISIBLE
                }
            }
        }

        private fun setDiaryContent() {
            post?.let {
                binding.location.text = it.locationName
                binding.content.text = it.content
                binding.likeCnt.text = it.likeCount.likeCount.toString()
                binding.dateAndTime.text = formatCreateAt(it.createAt)
                like.setLike(it.liked)

                if (it.categories.isNotEmpty()) {
                    if (it.categories.size == 2) {
                        binding.category2.visibility = View.VISIBLE
                        binding.category2.text =
                            itemView.context.getString(
                                R.string.category_text,
                                it.categories[1].categoryName
                            )
                    }
                    binding.category1.visibility = View.VISIBLE
                    binding.category1.text =
                        itemView.context.getString(
                            R.string.category_text,
                            it.categories[0].categoryName
                        )
                    binding.categories.visibility = View.VISIBLE
                }

                potatocake.katecam.everymoment.data.model.entity.Emotions.fromString(it.emoji)
                    ?.getEmotionUnicode()?.let { emotion ->
                        binding.emotion.text = emotion
                        binding.emotion.visibility = View.VISIBLE
                    }
                binding.header.visibility = View.VISIBLE
                if (!it.content.isNullOrBlank()) {
                    binding.content.visibility = View.VISIBLE
                }
            }
        }

        private fun formatCreateAt(createAt: String): String {
            val subCreatat = createAt.substring(0, 16)
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            isoFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date: Date = isoFormat.parse(subCreatat) ?: return ""
            val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.getDefault())
            return outputFormat.format(date)
        }

        private fun setClickListeners() {
            binding.like.setOnClickListener {
                viewModel.postLike()
                like.toggleLike()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_HEADER) {
            val binding = PostRecyclerHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return HeaderViewHolder(binding)
        } else {
            val binding =
                CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return comments.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind()
            }

            is ItemViewHolder -> {
                holder.bind(comments[position - 1])
            }
        }
    }

}