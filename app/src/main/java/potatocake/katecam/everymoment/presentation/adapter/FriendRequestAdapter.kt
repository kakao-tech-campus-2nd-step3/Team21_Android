package potatocake.katecam.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.dto.response.Member
import potatocake.katecam.everymoment.databinding.FriendRequestItemBinding
import potatocake.katecam.everymoment.extensions.CustomDialog
import potatocake.katecam.everymoment.presentation.viewModel.FriendRequestViewModel

class FriendRequestAdapter(
    private val activity: FragmentActivity,
    private val viewModel: FriendRequestViewModel
) : ListAdapter<Member, FriendRequestAdapter.FriendRequestViewHolder>(
    FriendRequestDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding =
            FriendRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding, activity, viewModel)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendRequestViewHolder(
        private val binding: FriendRequestItemBinding,
        private val activity: FragmentActivity,
        private val viewModel: FriendRequestViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Member) {
            binding.userNickname.text = user.nickname

            if (user.friendRequestStatus == "FRIEND") {
                binding.friendButton.visibility = VISIBLE
                binding.friendRequestButton.visibility = GONE
                binding.requestCompletedButton.visibility = GONE
            } else if (user.friendRequestStatus == "SELF") {
                binding.requestCompletedButton.visibility = GONE
                binding.friendButton.visibility = GONE
                binding.friendRequestButton.visibility = GONE
            } else if (user.friendRequestStatus == "SENT") {
                binding.requestCompletedButton.visibility = VISIBLE
                binding.friendButton.visibility = GONE
                binding.friendRequestButton.visibility = GONE
            } else {
                binding.friendButton.visibility = GONE
                binding.friendRequestButton.visibility = VISIBLE
                binding.requestCompletedButton.visibility = GONE
            }

            Glide.with(itemView.context)
                .load(user.profileImageUrl)
                .error(R.drawable.account_circle_24px)
                .fallback(R.drawable.account_circle_24px)
                .placeholder(R.drawable.account_circle_24px)
                .centerCrop()
                .into(binding.profile)

            binding.friendRequestButton.setOnClickListener {
                showFriendRequestConfirmationDialog(user)
            }
        }


        private fun showFriendRequestConfirmationDialog(user: Member) {
            CustomDialog("${user.nickname}님에게\n친구 신청을 하시겠습니까?", "취소", "신청", onPositiveClick = {
                viewModel.sendFriendRequest(user.id)
                binding.friendRequestButton.visibility = GONE
                binding.requestCompletedButton.visibility = VISIBLE
            }).show(activity.supportFragmentManager, "CustomDialog")
        }
    }

    class FriendRequestDiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem == newItem
        }
    }
}