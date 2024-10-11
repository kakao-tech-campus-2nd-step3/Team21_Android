package com.example.everymoment.presentation.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.data.model.Friends
import com.example.everymoment.databinding.FriendsListItemBinding
import com.example.everymoment.extensions.CustomDialog

class FriendsListAdapter(
    private val activity: FragmentActivity,
    private val onDeleteFriend: (Friends) -> Unit
) :
    ListAdapter<Friends, FriendsListAdapter.FriendViewHolder>(FriendDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding =
            FriendsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding, activity, onDeleteFriend)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendViewHolder(
        private val binding: FriendsListItemBinding,
        private val activity: FragmentActivity,
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
            CustomDialog(
                "${friends.name}님을\n친구에서 삭제하시겠습니까?",
                "아니오",
                "삭제하기",
                onPositiveClick = { onDeleteFriend(friends) }).show(activity.supportFragmentManager, "CustomDialog")
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