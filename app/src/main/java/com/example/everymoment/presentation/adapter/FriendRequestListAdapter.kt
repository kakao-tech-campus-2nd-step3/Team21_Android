package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.data.repository.FriendRequests
import com.example.everymoment.databinding.FriendRequestListItemBinding

class FriendRequestListAdapter(
    private val onAcceptClick: (FriendRequests) -> Unit,
    private val onRejectClick: (FriendRequests) -> Unit
) : ListAdapter<FriendRequests, FriendRequestListAdapter.FriendRequestListViewHolder>(
    FriendRequestListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestListViewHolder {
        val binding =
            FriendRequestListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendRequestListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun removeItem(position: Int) {
        val currentList = currentList.toMutableList()
        currentList.removeAt(position)
        submitList(currentList)
    }

    inner class FriendRequestListViewHolder(
        private val binding: FriendRequestListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friendRequest: FriendRequests) {
            binding.apply {
                userNickname.text = friendRequest.nickname

                friendRequestAcceptButton.setOnClickListener {
                    onAcceptClick(friendRequest)
                }

                friendRequestRefuseButton.setOnClickListener {
                    onRejectClick(friendRequest)
                }
            }
        }
    }

    class FriendRequestListDiffCallback : DiffUtil.ItemCallback<FriendRequests>() {
        override fun areItemsTheSame(oldItem: FriendRequests, newItem: FriendRequests): Boolean {
            return oldItem.senderId == newItem.senderId
        }

        override fun areContentsTheSame(oldItem: FriendRequests, newItem: FriendRequests): Boolean {
            return oldItem == newItem
        }
    }
}