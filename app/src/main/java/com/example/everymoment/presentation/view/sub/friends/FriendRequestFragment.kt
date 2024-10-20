package com.example.everymoment.presentation.view.sub.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.services.location.GlobalApplication
import com.example.everymoment.R
import com.example.everymoment.data.model.network.api.NetworkUtil
import com.example.everymoment.data.model.network.dto.response.Member
import com.example.everymoment.data.model.network.dto.response.MemberResponse
import com.example.everymoment.databinding.FragmentFriendRequestBinding
import com.example.everymoment.presentation.adapter.FriendRequestAdapter

class FriendRequestFragment : Fragment() {

    private lateinit var binding: FragmentFriendRequestBinding
    private lateinit var adapter: FriendRequestAdapter

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
        val token = GlobalApplication.prefs.getString("token", "Default")
        val jwtToken = token
        Log.d("memberNetwork", token)

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
        adapter = FriendRequestAdapter (requireActivity()) { user ->
        }
        binding.friendRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendRequestRecyclerView.adapter = adapter
        adapter.submitList(allMembers)
    }

    private fun setupSearch() {
        binding.searchUserEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()

                if (searchText.isEmpty()) {
                    // 검색어가 비어 있으면 RecyclerView를 숨기고 빈 화면을 표시합니다.
                    adapter.submitList(emptyList(), Runnable {
                        binding.friendRequestRecyclerView.visibility = View.GONE
                        binding.searchFriend.visibility = View.VISIBLE
                        binding.searchFriend.setHint(R.string.search_nothing)
                    })
                    return
                }

                // 검색어가 있을 때 필터링 실행
                val filteredList = allMembers.filter { it.nickname.contains(searchText, ignoreCase = true) }

                // 리스트가 업데이트된 후 콜백에서 UI를 조정
                adapter.submitList(filteredList, Runnable {
                    if (filteredList.isEmpty()) {
                        binding.friendRequestRecyclerView.visibility = View.GONE
                        binding.searchFriend.visibility = View.VISIBLE
                        binding.searchFriend.setHint(R.string.search_nothing)
                    } else {
                        binding.searchFriend.visibility = View.GONE
                        binding.friendRequestRecyclerView.visibility = View.VISIBLE
                    }
                })
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}