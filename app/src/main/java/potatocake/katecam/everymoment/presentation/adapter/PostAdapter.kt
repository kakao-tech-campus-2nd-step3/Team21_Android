package potatocake.katecam.everymoment.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.dto.response.getComments.Comment
import potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail.Post
import potatocake.katecam.everymoment.databinding.CommentItemBinding
import potatocake.katecam.everymoment.databinding.PostRecyclerHeaderBinding
import potatocake.katecam.everymoment.extensions.CustomDialog
import potatocake.katecam.everymoment.extensions.Like
import potatocake.katecam.everymoment.presentation.viewModel.PostViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostAdapter(private val context: Context, private val viewModel: PostViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var commentCount = 0
    private var likeCnt = 0
    private lateinit var delCommentDialog: CustomDialog

    private var post: Post? = null
    private var images: List<String>? = null
    private var comments: List<Comment> = listOf()

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }

    init {
        setDialog()
    }

    fun setDialog() {
        delCommentDialog = CustomDialog(
            message = context.getString(R.string.del_comment),
            negText = context.getString(R.string.cancel),
            posText = context.getString(R.string.delete),
            onPositiveClick = {

            }
        )
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
            setClickListeners()
        }

        fun setClickListeners() {
            binding.root.setOnLongClickListener {
                val adjustedPosition = adapterPosition - 1
                if (adjustedPosition >= 0) {
                    val commentId = comments[adjustedPosition].id
                    Log.d("dd", "$commentId")
                    viewModel.delComment(commentId)
                }
                true
            }
        }
    }

    inner class HeaderViewHolder(private val binding: PostRecyclerHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var like = Like(binding.like)

        init {
            binding.commentCnt.text = commentCount.toString()
        }

        fun bind() {
            setDiaryContent()
            setImages()
            setClickListeners()
        }

        private fun updateCommentCnt() {
            binding.commentCnt.text = commentCount.toString()
        }

        private fun setImages() {
            images?.let {
                if (it.isNotEmpty()) {
                    if (it.size == 2) {
                        binding.image2.visibility = View.VISIBLE
                        binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(context).load(it[1]).into(binding.image2)
                    }
                    binding.image1.visibility = View.VISIBLE
                    binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(context).load(it[0]).into(binding.image1)
                    binding.images.visibility = View.VISIBLE
                }
            }
        }

        private fun setDiaryContent() {
            post?.let {
                binding.location.text = it.locationName
                binding.content.text = it.content
                likeCnt = it.likeCount.likeCount
                binding.likeCnt.text = likeCnt.toString()
                binding.dateAndTime.text = formatCreateAt(it.createAt)
                like.setLike(it.liked)

                if (it.categories.isNotEmpty()) {
                    if (it.categories.size == 2) {
                        binding.category2.visibility = View.VISIBLE
                        binding.category2.text =
                            context.getString(R.string.category_text, it.categories[1].categoryName)
                    }
                    binding.category1.visibility = View.VISIBLE
                    binding.category1.text =
                        context.getString(R.string.category_text, it.categories[0].categoryName)
                    binding.categories.visibility = View.VISIBLE
                }

                potatocake.katecam.everymoment.data.model.entity.Emotions.fromString(it.emoji)?.getEmotionUnicode()?.let { emotion ->
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