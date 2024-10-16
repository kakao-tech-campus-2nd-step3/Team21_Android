package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.R
import com.example.everymoment.databinding.CategoryLayoutBinding

class CategoryAdapter() :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryList: List<String> = listOf("#힐링", "#공부", "#놀이", "#zzzzzzz", "#ddddd", "#ee")
    private var selectedCategories = mutableSetOf<Int>()


    fun resetSelected() {
        selectedCategories = mutableSetOf<Int>()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: CategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, isSelected: Boolean) {
            binding.category.text = item

            if (isSelected) {
                binding.category.setBackgroundResource(R.drawable.category_background)
                binding.category.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                binding.category.setBackgroundResource(R.drawable.category_gray_background)
                binding.category.setTextColor(ContextCompat.getColor(itemView.context, R.color.search_gray))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position], selectedCategories.contains(position))

        holder.itemView.setOnClickListener {
            if (selectedCategories.contains(holder.adapterPosition)) {
                selectedCategories.remove(holder.adapterPosition)
            } else {
                selectedCategories.add(holder.adapterPosition)
            }
            notifyItemChanged(holder.adapterPosition)
        }
    }
}