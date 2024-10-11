package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.data.model.FriendRequestList
import com.example.everymoment.databinding.FriendRequestListItemBinding

class FriendRequestListAdapter(
    private val onFriendRequestList: (FriendRequestList) -> Unit
) : ListAdapter<FriendRequestList, FriendRequestListAdapter.FriendRequestListViewHolder>(
    FriendRequestListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestListViewHolder {
        val binding =
            FriendRequestListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestListViewHolder(binding, onFriendRequestList)
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
        private val binding: FriendRequestListItemBinding,
        private val onFriendRequestList: (FriendRequestList) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FriendRequestList) {
            binding.userNickname.text = user.name

            binding.friendRequestAcceptButton.setOnClickListener {
                Toast.makeText(itemView.context, "${user.name}님의 친구요청을 수락했습니다.", Toast.LENGTH_SHORT).show()
                removeItem(adapterPosition)
            }

            binding.friendRequestRefuseButton.setOnClickListener {
                Toast.makeText(itemView.context, "${user.name}님의 친구요청을 거절했습니다.", Toast.LENGTH_SHORT).show()
                removeItem(adapterPosition)
            }
        }
    }

    class FriendRequestListDiffCallback : DiffUtil.ItemCallback<FriendRequestList>() {
        override fun areItemsTheSame(oldItem: FriendRequestList, newItem: FriendRequestList): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FriendRequestList, newItem: FriendRequestList): Boolean {
            return oldItem == newItem
        }
    }
}