package com.example.everymoment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.model.FriendRequestList
import com.example.everymoment.data.model.Friends
import com.example.everymoment.databinding.FragmentFriendRequestBinding
import com.example.everymoment.databinding.FragmentFriendRequestListBinding
import com.example.everymoment.presentation.adapter.FriendRequestAdapter
import com.example.everymoment.presentation.adapter.FriendRequestListAdapter
import com.example.everymoment.presentation.adapter.FriendsListAdapter


class FriendRequestListFragment : Fragment() {

    // 테스트용 더미 데이터
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
    )

    private lateinit var binding: FragmentFriendRequestListBinding
    private lateinit var adapter: FriendRequestListAdapter

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

        binding.friendRequestListBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendsListFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = FriendRequestListAdapter { friendRequest ->
        }
        binding.friendRequestListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendRequestListRecyclerView.adapter = adapter
        adapter.submitList(friendRequestMember.toList())
    }
}