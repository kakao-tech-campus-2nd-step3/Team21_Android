package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.R
import com.example.everymoment.data.model.Timeline
import com.example.everymoment.databinding.TimelineItemBinding

class TimelineAdapter(private val timelineList: MutableList<Timeline>): RecyclerView.Adapter<TimelineAdapter.Holder>() {

    inner class Holder(private val binding: TimelineItemBinding): RecyclerView.ViewHolder(binding.root) {
        val time = binding.timeText
        val buildingName = binding.locationNameText
        val address = binding.addressText

        val addEmojiButton = binding.selectedEmoji

        val emojiList = binding.emojiContainer
        val happyEmoji = binding.happyEmoji
        val laughEmoji = binding.laughEmoji
        val expressionlessEmoji = binding.expressionlessEmoji
        val annoyEmoji = binding.annoyEmoji
        val sadEmoji = binding.sadEmoji

        val diaryContainer = binding.detailedDiaryContainer

        val editButton = binding.editIcon
        val bookmarkButton = binding.bookmarkIcon
        val shareButton = binding.shareIcon
        val deleteButton = binding.deleteIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = TimelineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = timelineList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var isBookmarked = false
        var isShared = false

        holder.time.text = timelineList[position].time
        holder.buildingName.text = timelineList[position].buildingName
        holder.address.text = timelineList[position].address

        holder.addEmojiButton.setOnClickListener {
            if (holder.emojiList.visibility == View.GONE) {
                holder.emojiList.visibility = View.VISIBLE
                holder.emojiList.animate()
                    .translationX(0f)
                    .alpha(1.0f)
                    .setDuration(300)
                    .start()
            } else {
                // 메뉴가 보일 때 사라지게
                holder.emojiList.animate()
                    .translationX(-holder.emojiList.width.toFloat()) // 수평 이동
                    .alpha(0.0f) // 투명도 애니메이션
                    .setDuration(300) // 애니메이션 시간
                    .withEndAction {
                        holder.emojiList.visibility = View.GONE
                    }
                    .start()
            }
        }

        holder.bookmarkButton.setOnClickListener {
            if (isBookmarked) {
                holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark)
            } else {
                holder.bookmarkButton.setImageResource(R.drawable.ic_is_bookmarked)
            }

            isBookmarked = !isBookmarked
        }

        holder.shareButton.setOnClickListener {
            if (isShared) {
                holder.shareButton.setImageResource(R.drawable.ic_share)
            } else {
                holder.shareButton.setImageResource(R.drawable.ic_is_shared)
            }

            isShared = !isShared
        }

        holder.diaryContainer.isGone = !timelineList[position].isDetailedDiary

        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, id ->
                    timelineList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, timelineList.size)
                }
                .setNegativeButton("취소") { dialog, id ->
                    dialog.dismiss()
                }
            builder.create().show()
        }

        holder.editButton.setOnClickListener {
            val popupMenu = PopupMenu(holder.editButton.context, holder.editButton)
            popupMenu.menuInflater.inflate(R.menu.location_candidate_menu, popupMenu.menu)

            popupMenu.show()
        }
    }
}