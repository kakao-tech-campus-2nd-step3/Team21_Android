package com.example.everymoment.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.data.repository.Diary
import com.example.everymoment.databinding.TimelineItemBinding
import com.example.everymoment.extensions.EmotionPopup
import com.example.everymoment.extensions.ToPxConverter
import com.example.everymoment.presentation.viewModel.TimelineViewModel

class TimelineAdapter(private val viewModel: TimelineViewModel) : ListAdapter<Diary, TimelineAdapter.TimelineViewHolder>(
    object : DiffUtil.ItemCallback<Diary>() {
        override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class TimelineViewHolder(private val binding: TimelineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Diary) {
            binding.timeText.text = item.createAt.substring(11, 16)
            binding.locationNameText.text = item.locationName
            binding.addressText.text = item.address

            val emotionPopupManager = EmotionPopup(binding.root.context) { selectedEmotion ->
                binding.emotion.text = selectedEmotion.getEmotionUnicode()
                binding.addEmotion.visibility = View.GONE
                binding.emotion.visibility = View.VISIBLE
            }

            binding.addEmotion.setOnClickListener {
                emotionPopupManager.showEmotionsPopup(it, ToPxConverter().dpToPx(10))
            }

            binding.emotion.setOnClickListener {
                emotionPopupManager.showEmotionsPopup(it, ToPxConverter().dpToPx(10))
            }

            var isBookmarked = item.bookmark
            updateBookmarkIcon(isBookmarked)

            binding.bookmarkIcon.setOnClickListener {
                isBookmarked = !isBookmarked
                updateBookmarkIcon(isBookmarked)
                viewModel.updateBookmarkStatus(item.id)
                binding.root.context.showToast(
                    if (isBookmarked) R.string.add_bookmark else R.string.remove_bookmark
                )
            }

            var isShared = item.public
            updateShareIcon(isShared)

            binding.shareIcon.setOnClickListener {
                isShared = !isShared
                updateShareIcon(isShared)
                viewModel.updateShareStatus(item.id)
                binding.root.context.showToast(
                    if (isShared) R.string.is_public else R.string.is_private
                )
            }

            if (item.thumbnailResponse == null) {
                binding.detailedDiaryContainer.isGone = true
            } else {
                binding.detailedDiaryContainer.isVisible = true

                Glide.with(itemView.context)
                    .load(item.thumbnailResponse.imageUrl)
                    .into(binding.diaryImageContent)
            }

            binding.deleteIcon.setOnClickListener {
                AlertDialog.Builder(binding.root.context)
                    .setMessage("삭제하시겠습니까?")
                    .setPositiveButton("삭제") { _, _ ->
                        removeItem(adapterPosition)
                        viewModel.deleteDiary(item.id)
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }

            binding.editIcon.setOnClickListener {
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.menuInflater.inflate(R.menu.location_candidate_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    binding.locationNameText.text = menuItem.title
                    true
                }
                popupMenu.show()
            }
        }

        private fun updateBookmarkIcon(isBookmarked: Boolean) {
            binding.bookmarkIcon.setImageResource(
                if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.bookmark_26dp_5f6368_fill0_wght300_grad0_opsz24
            )
        }

        private fun updateShareIcon(isShared: Boolean) {
            binding.shareIcon.setImageResource(
                if (isShared) R.drawable.ic_is_shared else R.drawable.share_26dp_5f6368_fill0_wght300_grad0_opsz24
            )
        }

        private fun removeItem(position: Int) {
            val newList = currentList.toMutableList()
            newList.removeAt(position)
            submitList(newList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = TimelineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun Context.showToast(messageResId: Int) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show()
    }
}