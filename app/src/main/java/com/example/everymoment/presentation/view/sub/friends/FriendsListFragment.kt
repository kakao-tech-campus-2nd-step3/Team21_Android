package com.example.everymoment.presentation.view.sub.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.GlobalApplication
import com.example.everymoment.R
import com.example.everymoment.data.model.NetworkModule
import com.example.everymoment.data.model.PotatoCakeApiService
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.repository.Friends
import com.example.everymoment.databinding.FragmentFriendsListBinding
import com.example.everymoment.presentation.adapter.FriendsListAdapter
import com.example.everymoment.presentation.viewModel.FriendsListViewModel
import com.example.everymoment.presentation.viewModel.FriendsListViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FriendsListFragment : Fragment() {

    private lateinit var binding: FragmentFriendsListBinding
    private lateinit var adapter: FriendsListAdapter
    private var isFabExpanded = false

    private val viewModel: FriendsListViewModel by viewModels {
        FriendsListViewModelFactory(FriendRepository())
    }

    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_fab)
    }

    private val toBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_fab)
    }

    private val rotateClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clock_wise)
    }

    private val rotateAntiClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anti_clock_wise)
    }

    private var allFriends = mutableListOf<Friends>()

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
        observeViewModel()

        viewModel.fetchFriendsList()
    }

    private fun observeViewModel() {
        viewModel.friends.observe(viewLifecycleOwner) { friends ->
            allFriends.clear()
            allFriends.addAll(friends)
            updateAdapterList()
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
        }
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
        adapter = FriendsListAdapter(requireActivity()) { friend ->
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
                allFriends.filter { it.nickname.contains(searchText, ignoreCase = true) }
            adapter.submitList(filteredList)

            if (filteredList.isEmpty()) {
                binding.addFriend.visibility = View.VISIBLE
                binding.addFriend.hint = getString(R.string.search_nothing)
            } else {
                binding.addFriend.visibility = View.GONE
            }
        }
    }

    private fun deleteFriend(friend: Friends) {
        allFriends.remove(friend)
        updateAdapterList()
        viewModel.deleteFriend(friend.id)
    }

    private fun updateAdapterList() {
        adapter.submitList(allFriends.toList())
    }
}