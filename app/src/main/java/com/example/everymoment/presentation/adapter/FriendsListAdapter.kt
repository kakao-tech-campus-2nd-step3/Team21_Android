package com.example.everymoment.presentation.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.data.model.Friends
import com.example.everymoment.databinding.FriendsListItemBinding

class FriendsListAdapter(private val onDeleteFriend: (Friends) -> Unit) : ListAdapter<Friends, FriendsListAdapter.FriendViewHolder>(FriendDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = FriendsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding, onDeleteFriend)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendViewHolder(
        private val binding: FriendsListItemBinding,
        private val onDeleteFriend: (Friends) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friends: Friends) {
            binding.userNickname.text = friends.name

            itemView.setOnLongClickListener {
                showDeleteConfirmationDialog(friends)
                true
            }
        }

        private fun showDeleteConfirmationDialog(friends: Friends) {
            AlertDialog.Builder(itemView.context)
                .setTitle("친구 삭제")
                .setMessage("${friends.name}님을 친구에서 삭제하시겠습니까?")
                .setNegativeButton("아니오") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("네") { _, _ -> onDeleteFriend(friends) }
                .show()
        }
    }

    class FriendDiffCallback : DiffUtil.ItemCallback<Friends>() {
        override fun areItemsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem == newItem
        }
    }
}