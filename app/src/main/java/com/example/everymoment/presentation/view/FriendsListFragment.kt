package com.example.everymoment.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.model.Friends
import com.example.everymoment.databinding.FragmentFriendsListBinding
import com.example.everymoment.presentation.adapter.FriendsListAdapter

class FriendsListFragment : Fragment() {

    private lateinit var binding: FragmentFriendsListBinding
    private lateinit var adapter: FriendsListAdapter

    // 테스트용 더미 데이터
    private val friendsList = mutableListOf(
        Friends("박지연", "https://example.com/user1.jpg"),
        Friends("한예지", "https://example.com/user2.jpg"),
        Friends("춘식이", "https://example.com/user3.jpg"),
        Friends("소연이", "https://example.com/user4.jpg"),
        Friends("박소담", "https://example.com/user5.jpg"),
        Friends("한가인", "https://example.com/user6.jpg"),
        Friends("박지승", "https://example.com/user7.jpg"),
        Friends("김고은", "https://example.com/user8.jpg"),
        Friends("박소영", "https://example.com/user9.jpg"),
        Friends("김범수", "https://example.com/user10.jpg"),
        Friends("한혜진", "https://example.com/user11.jpg"),
        Friends("이소연", "https://example.com/user12.jpg"),
        Friends("이동욱", "https://example.com/user13.jpg")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()

        binding.friendAddButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendRequestFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = FriendsListAdapter { friend ->
            deleteFriend(friend)
        }
        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendsRecyclerView.adapter = adapter
        updateAdapterList()
    }

    private fun setupSearch() {
        binding.searchNicknameEditText.addTextChangedListener { editable ->
            val searchText = editable.toString()
            val filteredList =
                friendsList.filter { it.name.contains(searchText, ignoreCase = true) }
            adapter.submitList(filteredList)

            if (filteredList.isEmpty()) {
                binding.addFriend.visibility = View.VISIBLE
                binding.addFriend.setHint(R.string.search_nothing)
            } else {
                binding.addFriend.visibility = View.GONE
            }
        }
    }

    private fun deleteFriend(friend: Friends) {
        friendsList.remove(friend)
        updateAdapterList()
    }

    private fun updateAdapterList() {
        adapter.submitList(friendsList.toList())
    }
}