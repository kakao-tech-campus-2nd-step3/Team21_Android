package com.example.everymoment.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.model.NetworkUtil
import com.example.everymoment.data.repository.Member
import com.example.everymoment.data.repository.MemberResponse
import com.example.everymoment.databinding.FragmentFriendRequestBinding
import com.example.everymoment.presentation.adapter.FriendRequestAdapter

class FriendRequestFragment : Fragment() {

    private lateinit var binding: FragmentFriendRequestBinding
    private lateinit var adapter: FriendRequestAdapter

    // 테스트용 더미 데이터
    private var allMembers = mutableListOf<Member>()


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
        fetchMembersFromServer()

        binding.friendsBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendsListFragment())
                addToBackStack(null)
                commit()
            }
        }

    }

    private fun fetchMembersFromServer() {
        val url = "http://13.125.156.74:8080/api/members?size=30"
        val jwtToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NiwiaWF0IjoxNzI4NjA5ODk3LCJleHAiOjE3Mjg3ODI2OTd9.JaJ2Ut7M_YePTXZZNODRu6eGBXwbO2kLtDXl2jz9Ock"

        NetworkUtil.getData(
            url,
            jwtToken,
            responseClass = MemberResponse::class.java
        ) { success, memberResponse ->
            if (success && memberResponse != null) {
                Log.d("memberNetwork", "fetchedMemberList : ${memberResponse.info.members}")

                allMembers.clear()
                allMembers.addAll(memberResponse.info.members)
                Log.d("memberNetwork", "allMembers list: $allMembers")

                activity?.runOnUiThread {
                    adapter.submitList(allMembers)
                    setupRecyclerView()
                }
            } else {
                Log.d("memberNetwork", "Network failed")
                activity?.runOnUiThread {

                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = FriendRequestAdapter { user ->
        }
        binding.friendRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendRequestRecyclerView.adapter = adapter
        adapter.submitList(allMembers)
    }

    private fun setupSearch() {
        binding.searchUserEditText.addTextChangedListener { editable ->
            val searchText = editable.toString()
            val filteredList =
                allMembers.filter { it.nickname.contains(searchText, ignoreCase = true) }
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