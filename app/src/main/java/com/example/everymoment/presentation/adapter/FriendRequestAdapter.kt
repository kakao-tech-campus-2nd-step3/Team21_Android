package com.example.everymoment.presentation.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.data.model.FriendRequest
import com.example.everymoment.databinding.FriendRequestItemBinding

class FriendRequestAdapter(
    private val onFriendRequest: (FriendRequest) -> Unit
) : ListAdapter<FriendRequest, FriendRequestAdapter.FriendRequestViewHolder>(
    FriendRequestDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding =
            FriendRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding, onFriendRequest)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendRequestViewHolder(
        private val binding: FriendRequestItemBinding,
        private val onFriendRequest: (FriendRequest) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FriendRequest) {
            binding.userNickname.text = user.name

            binding.friendRequestButton.setOnClickListener {
                showFriendRequestConfirmationDialog(user)
            }
        }

        private fun showFriendRequestConfirmationDialog(user: FriendRequest) {
            AlertDialog.Builder(itemView.context)
                .setTitle("친구 신청")
                .setMessage("${user.name}님에게 친구 신청을 하시겠습니까?")
                .setNegativeButton("아니오") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("네") { _, _ ->
                    onFriendRequest(user)
                    binding.friendRequestButton.visibility = View.GONE
                    binding.requestCompletedButton.visibility = View.VISIBLE
                }
                .show()
        }
    }

    class FriendRequestDiffCallback : DiffUtil.ItemCallback<FriendRequest>() {
        override fun areItemsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
            return oldItem == newItem
        }
    }
}