package com.example.everymoment.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.R
import com.example.everymoment.data.model.Timeline
import com.example.everymoment.databinding.TimelineItemBinding
import com.example.everymoment.extensions.EmotionPopup
import com.example.everymoment.extensions.ToPxConverter

class TimelineAdapter(private val timelineList: MutableList<Timeline>): RecyclerView.Adapter<TimelineAdapter.Holder>() {

    inner class Holder(private val binding: TimelineItemBinding): RecyclerView.ViewHolder(binding.root) {
        val time = binding.timeText
        val buildingName = binding.locationNameText
        val address = binding.addressText

        val addEmotion = binding.addEmotion
        val emotion = binding.emotion

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

        val emotionPopupManager = EmotionPopup(holder.itemView.context) { selectedEmotion ->
            holder.emotion.text = selectedEmotion.getEmotionUnicode()
            holder.addEmotion.visibility = View.GONE
            holder.emotion.visibility = View.VISIBLE
        }

        holder.addEmotion.setOnClickListener {
            emotionPopupManager.showEmotionsPopup(holder.addEmotion, ToPxConverter().dpToPx(10))
        }

        holder.emotion.setOnClickListener {
            emotionPopupManager.showEmotionsPopup(holder.emotion, ToPxConverter().dpToPx(10))
        }

        holder.bookmarkButton.setOnClickListener {
            if (isBookmarked) {
                holder.bookmarkButton.setImageResource(R.drawable.baseline_bookmark_border_24)
                holder.itemView.context.showToast(holder.itemView.context.getString(R.string.remove_bookmark))
            } else {
                holder.bookmarkButton.setImageResource(R.drawable.baseline_bookmark_24)
                holder.itemView.context.showToast(holder.itemView.context.getString(R.string.add_bookmark))
            }

            isBookmarked = !isBookmarked
        }

        holder.shareButton.setOnClickListener {
            if (isShared) {
                holder.shareButton.setImageResource(R.drawable.ic_share)
                holder.itemView.context.showToast(holder.itemView.context.getString(R.string.is_private))
            } else {
                holder.shareButton.setImageResource(R.drawable.ic_is_shared)
                holder.itemView.context.showToast(holder.itemView.context.getString(R.string.is_public))
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

            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                holder.buildingName.text = item.title.toString()
                true
            }
            popupMenu.show()
        }
    }

    private fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}