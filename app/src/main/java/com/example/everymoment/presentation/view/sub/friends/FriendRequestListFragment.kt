package com.example.everymoment.presentation.view.sub.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.model.network.dto.response.FriendRequests
import com.example.everymoment.databinding.FragmentFriendRequestListBinding
import com.example.everymoment.presentation.adapter.FriendRequestListAdapter
import com.example.everymoment.presentation.viewModel.FriendRequestListViewModel
import com.example.everymoment.presentation.viewModel.factory.FriendRequestListViewModelFactory


class FriendRequestListFragment : Fragment() {

    private lateinit var binding: FragmentFriendRequestListBinding
    private lateinit var adapter: FriendRequestListAdapter
    private val viewModel: FriendRequestListViewModel by viewModels {
        FriendRequestListViewModelFactory(FriendRepository())
    }
    private var allRequestedFriend = mutableListOf<FriendRequests>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendRequestListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchFriendRequestList()

        binding.friendRequestListBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendsListFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.requestedFriend.observe(viewLifecycleOwner) { requestedFriends ->
            allRequestedFriend.clear()
            allRequestedFriend.addAll(requestedFriends)
            updateAdapterList()

            if (allRequestedFriend.isEmpty()) {
                binding.searchFriend.visibility = View.VISIBLE
                binding.searchFriend.hint = getString(R.string.no_friend_request)
            } else {
                binding.searchFriend.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = FriendRequestListAdapter(
            onAcceptClick = { friendRequest ->
                viewModel.acceptFriendRequest(friendRequest.id)
                Toast.makeText(
                    requireContext(),
                    "${friendRequest.nickname}님의 친구요청을 수락했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onRejectClick = { friendRequest ->
                viewModel.rejectFriendRequest(friendRequest.id)
                Toast.makeText(
                    requireContext(),
                    "${friendRequest.nickname}님의 친구요청을 거절했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.friendRequestListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendRequestListRecyclerView.adapter = adapter
    }

    private fun updateAdapterList() {
        adapter.submitList(allRequestedFriend.toList())
    }
}