package com.example.everymoment.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.databinding.CommentItemBinding
import com.example.everymoment.databinding.PostRecyclerHeaderBinding

class PostAdapter(private val context: Context, private val onPostClick: (String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLike: Boolean = false
    private var likeCount = 0
    private var commentList: MutableList<Array<String>> = mutableListOf()

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return VIEW_TYPE_HEADER
        else return VIEW_TYPE_ITEM
    }

    inner class ItemViewHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sArray: Array<String>) {
            binding.nickName.text = sArray[1]
            binding.comment.text = sArray[2]
        }

    }

    inner class HeaderViewHolder(private val binding: PostRecyclerHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.likeCnt.text = likeCount.toString()
        }

        fun bind() {
            binding.like.setOnClickListener {
                if (isLike) {
                    binding.like.setImageResource(R.drawable.favorite_24px)
                    isLike = false
                    likeCount -= 1
                } else {
                    binding.like.setImageResource(R.drawable.favorite_fill_24px)
                    isLike = true
                    likeCount += 1
                }
                binding.likeCnt.text = likeCount.toString()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_HEADER) {
            val binding = PostRecyclerHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return HeaderViewHolder(binding)
        } else {
            val binding =
                CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return commentList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind()
            }

            is ItemViewHolder -> {
                holder.bind(commentList[position - 1])
            }
        }
    }

}