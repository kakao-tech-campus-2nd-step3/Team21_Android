package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.data.model.NotificationTypeConstants
import com.example.everymoment.data.repository.MyNotification
import com.example.everymoment.databinding.NotificationItemBinding
import com.example.everymoment.presentation.viewModel.NotificationViewModel

class NotificationAdapter(private val viewModel: NotificationViewModel) :
    ListAdapter<MyNotification, NotificationAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<MyNotification>() {
            override fun areItemsTheSame(oldItem: MyNotification, newItem: MyNotification): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyNotification, newItem: MyNotification): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    inner class ViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyNotification) {
            binding.message.text = item.content

            if (item.type == NotificationTypeConstants.FRIEND_REQUEST) {
                binding.posButton.isVisible = true
                binding.negButton.isVisible = true

                binding.posButton.setOnClickListener { viewModel.acceptFriendRequest(item.targetId) }
                binding.negButton.setOnClickListener { viewModel.rejectFriendRequest(item.targetId) }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}