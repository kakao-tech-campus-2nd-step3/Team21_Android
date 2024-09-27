package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.databinding.NotificationLayoutBinding

class NotificationAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private var notiList: List<String> = emptyList()

    inner class ViewHolder(private val binding: NotificationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(s: String) {

        }

        init {
            binding.root.setOnClickListener { }
            binding.posButton.setOnClickListener { onClick(notiList[adapterPosition]) }
            binding.negButton.setOnClickListener { onClick(notiList[adapterPosition]) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NotificationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notiList[position])
    }

}