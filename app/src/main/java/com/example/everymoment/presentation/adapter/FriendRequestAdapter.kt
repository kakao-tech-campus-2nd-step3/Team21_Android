package com.example.everymoment.presentation.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.data.repository.Member
import com.example.everymoment.databinding.FriendRequestItemBinding
import com.example.everymoment.extensions.CustomDialog

class FriendRequestAdapter(
    private val activity: FragmentActivity,
    private val onFriendRequest: (Member) -> Unit
) : ListAdapter<Member, FriendRequestAdapter.FriendRequestViewHolder>(
    FriendRequestDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding =
            FriendRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding, activity, onFriendRequest)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendRequestViewHolder(
        private val binding: FriendRequestItemBinding,
        private val activity: FragmentActivity,
        private val onFriendRequest: (Member) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Member) {
            binding.userNickname.text = user.nickname

            binding.friendRequestButton.setOnClickListener {
                showFriendRequestConfirmationDialog(user)
            }
        }

        private fun showFriendRequestConfirmationDialog(user: Member) {
            CustomDialog("${user.nickname}님에게\n친구 신청을 하시겠습니까?", "취소", "신청", onPositiveClick = {
                onFriendRequest(user)
                binding.friendRequestButton.visibility = View.GONE
                binding.requestCompletedButton.visibility = View.VISIBLE
            }).show(activity.supportFragmentManager, "CustomDialog")
        }
    }

    class FriendRequestDiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.nickname == newItem.nickname
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem == newItem
        }
    }
}