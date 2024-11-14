package potatocake.katecam.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.dto.response.Friends
import potatocake.katecam.everymoment.databinding.FriendItemBinding
import potatocake.katecam.everymoment.presentation.viewModel.ShareViewModel

class SharedFriendListAdapter(private val viewModel: ShareViewModel) : ListAdapter<Friends, SharedFriendListAdapter.SharedFriendListViewHolder>(
    object : DiffUtil.ItemCallback<Friends>() {
        override fun areItemsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem == newItem
        }
    }
) {
    private var selectedPosition = RecyclerView.NO_POSITION

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        viewModel.fetchFriendDiaryList(getItem((position)).id)
        viewModel.setSelectedFriendName(getItem(position).nickname)
        viewModel.setSelectedFriendPosition(position)
        notifyItemChanged(position)
    }

    inner class SharedFriendListViewHolder(private val binding: FriendItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Friends) {
            binding.friendName.text = item.nickname

            if (item.profileImageUrl == null) {
                binding.friendImage.setImageResource(R.drawable.account_circle_24px)
            } else {
                Glide.with(itemView.context)
                    .load(item.profileImageUrl)
                    .circleCrop()
                    .into(binding.friendImage)
            }

            updateRingBackground(adapterPosition == selectedPosition)


            binding.friendContainer.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val oldPosition = selectedPosition
                    selectedPosition = if (selectedPosition == position) {
                        position
                    } else {
                        position
                    }

                    if (oldPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(oldPosition)
                    }
                    notifyItemChanged(position)
                    viewModel.fetchFriendDiaryList(item.id)
                    viewModel.setSelectedFriendName(item.nickname)
                    viewModel.setSelectedFriendPosition(position)
                }
            }
        }

        private fun updateRingBackground(isSelected: Boolean) {
            binding.storyRing.setBackgroundResource(
                if (isSelected) R.drawable.story_ring_updated
                else R.drawable.story_ring_not_updated
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedFriendListViewHolder {
        val binding = FriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SharedFriendListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SharedFriendListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}