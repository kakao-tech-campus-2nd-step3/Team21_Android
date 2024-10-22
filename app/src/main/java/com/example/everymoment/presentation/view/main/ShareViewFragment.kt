package com.example.everymoment.presentation.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.repository.Diary
import com.example.everymoment.data.repository.FriendDiaryRepository
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.repository.Member
import com.example.everymoment.data.repository.ThumbnailResponse
import com.example.everymoment.databinding.FragmentShareViewBinding
import com.example.everymoment.presentation.adapter.FriendsListAdapter
import com.example.everymoment.presentation.adapter.SharedFriendDiaryListAdapter
import com.example.everymoment.presentation.adapter.SharedFriendListAdapter
import com.example.everymoment.presentation.view.sub.friends.FriendsListFragment
import com.example.everymoment.presentation.viewModel.ShareViewModel
import com.example.everymoment.presentation.viewModel.ShareViewModelFactory
import com.example.everymoment.presentation.viewModel.TimelineViewModel
import com.example.everymoment.presentation.viewModel.TimelineViewModelFactory
import com.kakao.sdk.talk.model.Friend
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ShareViewFragment : Fragment() {
    private lateinit var binding: FragmentShareViewBinding
    private lateinit var viewModel: ShareViewModel
    private val friendDiaryRepository = FriendDiaryRepository()
    private val friendRepository = FriendRepository()
    private val calendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ShareViewModelFactory(friendDiaryRepository, friendRepository)).get(ShareViewModel::class.java)

        updateDateText()

        val friendListAdapter = SharedFriendListAdapter(viewModel)
        val friendDiaryAdapter = SharedFriendDiaryListAdapter()
        setupRecyclerView(friendListAdapter, friendDiaryAdapter)
        observeFriendList(friendListAdapter)
        observeFriendDiaryList(friendDiaryAdapter)

        val initialDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        viewModel.fetchFriendsList()
        viewModel.fetchTotalFriendDiaryList(initialDate)

        binding.friendListIcon.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendsListFragment())
                addToBackStack(null)
                commit()
            }
        }

        binding.nextDate.setOnClickListener {
            calendar.add(Calendar.DATE, 1)
            updateDateText()
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            viewModel.fetchTotalFriendDiaryList(currentDate)
        }

        binding.prevDate.setOnClickListener {
            calendar.add(Calendar.DATE, -1)
            updateDateText()
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            viewModel.fetchTotalFriendDiaryList(currentDate)
        }
    }

    private fun observeFriendDiaryList(adapter: SharedFriendDiaryListAdapter) {
        viewModel.diaries.observe(viewLifecycleOwner) { friendDiaryList ->
            adapter.submitList(friendDiaryList)
        }
    }

    private fun observeFriendList(adapter: SharedFriendListAdapter) {
        viewModel.friends.observe(viewLifecycleOwner) { friendList ->
            adapter.submitList(friendList)
        }
    }

    private fun setupRecyclerView(adapter1: SharedFriendListAdapter, adapter2: SharedFriendDiaryListAdapter) {
        binding.friendList.adapter = adapter1
        binding.timeLineRecyclerView.adapter = adapter2

        binding.friendList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.timeLineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateDateText() {
        val formattedDate = SimpleDateFormat("M월 d일 (E)", Locale("ko", "KR")).format(calendar.time)
        binding.currentDate.text = formattedDate
    }
}