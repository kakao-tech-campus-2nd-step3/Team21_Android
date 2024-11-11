package potatocake.katecam.everymoment.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.dto.response.Diary
import potatocake.katecam.everymoment.databinding.SearchItemBinding
import potatocake.katecam.everymoment.presentation.viewModel.SearchViewModel

class SearchAdapter(private val activity: FragmentActivity, private val viewModel: SearchViewModel) : ListAdapter<Diary, SearchAdapter.SearchViewHolder>(
    object : DiffUtil.ItemCallback<Diary>() {
        override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class SearchViewHolder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Diary) {

            binding.dayText.text = item.createAt.substring(0, 10)
            binding.timeText.text = item.createAt.substring(11, 16)
            binding.locationNameText.text = item.locationName
            binding.addressText.text = item.address

            if (item.emoji == null){
                binding.emotion.visibility = View.GONE
            } else {
                binding.emotion.visibility = View.VISIBLE
                binding.emotion.text = potatocake.katecam.everymoment.data.model.entity.Emotions.fromString(item.emoji)?.getEmotionUnicode()
            }

            var isBookmarked = item.bookmark
            updateBookmarkIcon(isBookmarked)

            var isShared = item.public
            updateShareIcon(isShared)

            if (item.thumbnailResponse == null) {
                binding.detailedDiaryContainer.isGone = true
            } else {
                binding.detailedDiaryContainer.isVisible = true
                binding.diaryImageContent.isVisible = true

                Glide.with(itemView.context)
                    .load(item.thumbnailResponse.imageUrl)
                    .into(binding.diaryImageContent)
            }

            if (item.content == null) {
                binding.diaryTextContent.isGone = true
            } else {
                binding.diaryTextContent.isVisible = true
                binding.diaryTextContent.text = item.content
            }
        }

        private fun updateBookmarkIcon(isBookmarked: Boolean) {
            binding.bookmarkIcon.setImageResource(
                if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.bookmark_26dp_5f6368_fill0_wght300_grad0_opsz24
            )
        }

        private fun updateShareIcon(isShared: Boolean) {
            binding.shareIcon.setImageResource(
                if (isShared) R.drawable.ic_is_shared else R.drawable.share_26dp_5f6368_fill0_wght300_grad0_opsz24
            )
        }

        private fun removeItem(position: Int) {
            val newList = currentList.toMutableList()
            newList.removeAt(position)
            submitList(newList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun Context.showToast(messageResId: Int) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show()
    }
}