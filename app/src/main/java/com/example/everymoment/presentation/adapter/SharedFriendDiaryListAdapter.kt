package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.data.repository.Diary
import com.example.everymoment.databinding.ShareItemBinding

class SharedFriendDiaryListAdapter : ListAdapter<Diary, SharedFriendDiaryListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Diary>() {
        override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class ViewHolder(private val binding: ShareItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Diary) {
            binding.timeText.text = item.createAt
            binding.locationNameText.text = item.locationName
            binding.addressText.text = item.address

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}