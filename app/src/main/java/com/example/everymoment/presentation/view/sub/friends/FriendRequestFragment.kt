package com.example.everymoment.presentation.view.sub.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.model.network.api.NetworkUtil
import com.example.everymoment.data.model.network.dto.response.Member
import com.example.everymoment.data.model.network.dto.response.MemberResponse
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.databinding.FragmentFriendRequestBinding
import com.example.everymoment.presentation.adapter.FriendRequestAdapter
import com.example.everymoment.presentation.viewModel.FriendRequestViewModel
import com.example.everymoment.presentation.viewModel.factory.FriendRequestViewModelFactory
import com.example.everymoment.services.location.GlobalApplication

class FriendRequestFragment : Fragment() {

    private lateinit var binding: FragmentFriendRequestBinding
    private lateinit var adapter: FriendRequestAdapter
    private val viewModel: FriendRequestViewModel by viewModels {
        FriendRequestViewModelFactory(FriendRepository())
    }

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
        observeViewModel()
        viewModel.fetchMembers()


        binding.friendsBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendsListFragment())
                addToBackStack(null)
                commit()
            }
        }

    }

    private fun observeViewModel() {
        viewModel.members.observe(viewLifecycleOwner) { members ->
            allMembers.clear()
            allMembers.addAll(members)
            updateAdapterList()
        }
    }

    private fun setupRecyclerView() {
        adapter = FriendRequestAdapter (requireActivity()) { user ->
        }
        binding.friendRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendRequestRecyclerView.adapter = adapter
        updateAdapterList()
    }

    private fun setupSearch() {
        binding.searchUserEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()

                if (searchText.isEmpty()) {
                    binding.searchFriend.visibility = View.VISIBLE
                    binding.searchFriend.setHint(R.string.search_nothing)
                    return
                }

                val filteredList = allMembers.filter {
                    it.nickname.contains(searchText, ignoreCase = true)
                }

                adapter.submitList(filteredList) {
                    if (filteredList.isEmpty()) {
                        binding.friendRequestRecyclerView.visibility = View.GONE
                        binding.searchFriend.visibility = View.VISIBLE
                        binding.searchFriend.setHint(R.string.search_nothing)
                    } else {
                        binding.friendRequestRecyclerView.visibility = View.VISIBLE
                        binding.searchFriend.visibility = View.GONE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateAdapterList() {
        adapter.submitList(allMembers.toList())
    }
}