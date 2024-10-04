package com.example.everymoment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.model.FriendRequest
import com.example.everymoment.data.model.Friends
import com.example.everymoment.databinding.FragmentFriendRequestBinding
import com.example.everymoment.presentation.adapter.FriendRequestAdapter

class FriendRequestFragment : Fragment() {

    private lateinit var binding: FragmentFriendRequestBinding
    private lateinit var adapter: FriendRequestAdapter

    // 테스트용 더미 데이터
    private val allUsers = mutableListOf(
        FriendRequest("박지연", "https://example.com/user1.jpg"),
        FriendRequest("한예지", "https://example.com/user2.jpg"),
        FriendRequest("춘식이", "https://example.com/user3.jpg"),
        FriendRequest("소연이", "https://example.com/user4.jpg"),
        FriendRequest("박소담", "https://example.com/user5.jpg"),
        FriendRequest("한가인", "https://example.com/user6.jpg"),
        FriendRequest("박지승", "https://example.com/user7.jpg"),
        FriendRequest("김고은", "https://example.com/user8.jpg"),
        FriendRequest("박소영", "https://example.com/user9.jpg"),
        FriendRequest("김범수", "https://example.com/user10.jpg"),
        FriendRequest("한혜진", "https://example.com/user11.jpg")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()

        binding.friendsBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendsListFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = FriendRequestAdapter { user ->
        }
        binding.friendRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendRequestRecyclerView.adapter = adapter
        adapter.submitList(allUsers)
    }

    private fun setupSearch() {
        binding.searchUserEditText.addTextChangedListener { editable ->
            val searchText = editable.toString()
            val filteredList = allUsers.filter { it.name.contains(searchText, ignoreCase = true) }
            adapter.submitList(filteredList)

            if (filteredList.isEmpty()) {
                binding.searchFriend.visibility = View.VISIBLE
                binding.searchFriend.setHint(R.string.search_nothing)
            } else {
                binding.searchFriend.visibility = View.GONE
            }
        }
    }
}