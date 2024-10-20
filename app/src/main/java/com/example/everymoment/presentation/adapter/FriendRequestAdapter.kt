package com.example.everymoment.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everymoment.services.location.GlobalApplication
import com.example.everymoment.R
import com.example.everymoment.data.model.network.api.NetworkModule
import com.example.everymoment.data.model.network.api.PotatoCakeApiService
import com.example.everymoment.data.model.network.dto.response.Member
import com.example.everymoment.data.model.network.dto.response.MemberResponse
import com.example.everymoment.databinding.FriendRequestItemBinding
import com.example.everymoment.extensions.CustomDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        private val apiService: PotatoCakeApiService = NetworkModule.provideApiService(NetworkModule.provideRetrofit())
        private val jwtToken = GlobalApplication.prefs.getString("token", "null")
        private val token = "Bearer $jwtToken"
        fun bind(user: Member) {
            binding.userNickname.text = user.nickname

            if (user.profileImageUrl == null) {
                binding.profile.setImageResource(R.drawable.account_circle_24px)
            } else {
                Glide.with(itemView.context)
                    .load(user.profileImageUrl)
                    .into(binding.profile)
            }

            binding.friendRequestButton.setOnClickListener {
                showFriendRequestConfirmationDialog(user)
            }
        }



        private fun showFriendRequestConfirmationDialog(user: Member) {
            CustomDialog("${user.nickname}님에게\n친구 신청을 하시겠습니까?", "아니오", "신청하기", onPositiveClick = {
                sendFriendRequest(user.id) { success, _ ->
                    if (success) {
                        activity.runOnUiThread {
                            onFriendRequest(user)
                            binding.friendRequestButton.visibility = View.GONE
                            binding.requestCompletedButton.visibility = View.VISIBLE
                        }
                    } else {
                        activity.runOnUiThread {

                        }
                    }
                }
            }).show(activity.supportFragmentManager, "CustomDialog")
        }

        fun sendFriendRequest(
            memberId: Int,
            callback: (Boolean, MemberResponse?) -> Unit
        ) {
            apiService.sendFriendRequest(token, memberId).enqueue(object : Callback<MemberResponse> {
                override fun onResponse(p0: Call<MemberResponse>, p1: Response<MemberResponse>) {
                    if (p1.isSuccessful) {
                        Log.d("FriendRequestPost", "${p1.body()}")
                        callback(true, p1.body())
                    } else {
                        callback(false, null)
                    }
                }

                override fun onFailure(p0: Call<MemberResponse>, p1: Throwable) {
                    Log.d("FriendRequestPost", "Failed to fetch diaries: ${p1.message}")
                    callback(false, null)
                }
            })
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