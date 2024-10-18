package com.example.everymoment.presentation.view.sub.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.repository.FriendRequests
import com.example.everymoment.databinding.FragmentFriendRequestListBinding
import com.example.everymoment.presentation.adapter.FriendRequestListAdapter
import com.example.everymoment.presentation.viewModel.FriendRequestListViewModel
import com.example.everymoment.presentation.viewModel.FriendRequestListViewModelFactory
import com.google.android.material.snackbar.Snackbar


class FriendRequestListFragment : Fragment() {

/*    // 테스트용 더미 데이터
    private val friendRequestMember = mutableListOf(
        FriendRequestList("박지연", "https://example.com/user1.jpg"),
        FriendRequestList("한예지", "https://example.com/user2.jpg"),
        FriendRequestList("춘식이", "https://example.com/user3.jpg"),
        FriendRequestList("소연이", "https://example.com/user4.jpg"),
        FriendRequestList("박소담", "https://example.com/user5.jpg"),
        FriendRequestList("한가인", "https://example.com/user6.jpg"),
        FriendRequestList("박지승", "https://example.com/user7.jpg"),
        FriendRequestList("김고은", "https://example.com/user8.jpg"),
        FriendRequestList("박소영", "https://example.com/user9.jpg"),
        FriendRequestList("김범수", "https://example.com/user10.jpg"),
        FriendRequestList("한혜진", "https://example.com/user11.jpg")
    )*/

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
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
        }
    }
    private fun setupRecyclerView() {
        adapter = FriendRequestListAdapter { friendRequest ->
        }
        binding.friendRequestListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendRequestListRecyclerView.adapter = adapter
        updateAdapterList()
    }

    private fun updateAdapterList() {
        adapter.submitList(allRequestedFriend.toList())
    }
}