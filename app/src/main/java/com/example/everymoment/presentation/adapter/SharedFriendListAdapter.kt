package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.data.repository.Member
import com.example.everymoment.databinding.FriendItemBinding

class SharedFriendListAdapter : ListAdapter<Member, SharedFriendListAdapter.SharedFriendListViewHolder>(
    object : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class SharedFriendListViewHolder(private val binding: FriendItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Member) {
            binding.friendName.text = item.nickname
            // binding.friendImage.setImageResource(item.profileImageUrl)

            binding.friendContainer.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedFriendListViewHolder {
        val binding = FriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SharedFriendListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SharedFriendListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}