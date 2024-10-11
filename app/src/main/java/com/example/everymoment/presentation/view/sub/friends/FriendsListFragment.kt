package com.example.everymoment.presentation.view.sub.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
    private var isFabExpanded = false

    private val fromBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_fab)
    }

    private val toBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_fab)
    }

    private val rotateClockWiseFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clock_wise)
    }

    private val rotateAntiClockWiseFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anti_clock_wise)
    }

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
        Friends("한혜진", "https://example.com/user11.jpg")
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

        binding.mainFab.setOnClickListener {
            if (isFabExpanded) {
                shrinkFab()
            } else {
                expandFab()
            }
        }

        binding.friendRequestFab.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendRequestFragment())
                addToBackStack(null)
                commit()
            }
        }

        binding.friendAcceptFab.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendRequestListFragment())
                addToBackStack(null)
                commit()
            }
        }

        setupRecyclerView()
        setupSearch()


    }

    private fun expandFab() {

        binding.mainFab.startAnimation(rotateClockWiseFabAnim)
        binding.friendRequestFab.startAnimation(fromBottomFabAnim)
        binding.friendAcceptFab.startAnimation(fromBottomFabAnim)
        binding.friendRequestFabTv.startAnimation(fromBottomFabAnim)
        binding.friendAcceptFabTv.startAnimation(fromBottomFabAnim)

        binding.friendRequestFabTv.visibility = View.VISIBLE
        binding.friendAcceptFabTv.visibility = View.VISIBLE
        binding.friendRequestFab.visibility = View.VISIBLE
        binding.friendRequestFab.isClickable = true
        binding.friendAcceptFab.visibility = View.VISIBLE
        binding.friendAcceptFab.isClickable = true
        isFabExpanded = !isFabExpanded
    }

    private fun shrinkFab() {

        binding.mainFab.startAnimation(rotateAntiClockWiseFabAnim)
        binding.friendRequestFab.startAnimation(toBottomFabAnim)
        binding.friendAcceptFab.startAnimation(toBottomFabAnim)
        binding.friendRequestFabTv.startAnimation(toBottomFabAnim)
        binding.friendAcceptFabTv.startAnimation(toBottomFabAnim)

        binding.friendRequestFabTv.visibility = View.GONE
        binding.friendAcceptFabTv.visibility = View.GONE
        binding.friendRequestFab.visibility = View.GONE
        binding.friendRequestFab.isClickable = false
        binding.friendAcceptFab.visibility = View.GONE
        binding.friendAcceptFab.isClickable = false

        isFabExpanded = !isFabExpanded
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