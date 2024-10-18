package com.example.everymoment.presentation.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.data.repository.Friends
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
            binding.userNickname.text = friends.nickname

            if (friends.profileImageUrl == null) {
                binding.profile.setImageResource(R.drawable.account_circle_24px)
            } else {
                Glide.with(itemView.context)
                    .load(friends.profileImageUrl)
                    .into(binding.profile)
            }

            itemView.setOnLongClickListener {
                showDeleteConfirmationDialog(friends)
                true
            }
        }

        private fun showDeleteConfirmationDialog(friends: Friends) {
            CustomDialog(
                "${friends.nickname}님을\n친구에서 삭제하시겠습니까?",
                "아니오",
                "삭제하기",
                onPositiveClick = { onDeleteFriend(friends) }).show(activity.supportFragmentManager, "CustomDialog")
        }
    }

    class FriendDiffCallback : DiffUtil.ItemCallback<Friends>() {
        override fun areItemsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem == newItem
        }
    }
}