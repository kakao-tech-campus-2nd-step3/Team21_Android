package com.example.everymoment.presentation.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.repository.Diary
import com.example.everymoment.data.repository.Member
import com.example.everymoment.data.repository.Thumbnail
import com.example.everymoment.databinding.FragmentShareViewBinding
import com.example.everymoment.presentation.adapter.SharedFriendDiaryListAdapter
import com.example.everymoment.presentation.adapter.SharedFriendListAdapter
import com.example.everymoment.presentation.view.sub.friends.FriendsListFragment

class ShareViewFragment : Fragment() {
    private lateinit var binding: FragmentShareViewBinding

    private val friendAdapter = SharedFriendListAdapter()
    private val diaryAdapter = SharedFriendDiaryListAdapter()
    private var friendList: MutableList<Member> = mutableListOf()
    private var diaryList: MutableList<Diary> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        // dummyData1
        friendList.add(Member(1, "url", "춘식이"))
        friendList.add(Member(2, "url", "제이지"))
        friendList.add(Member(3, "url", "프로도"))
        friendList.add(Member(4, "url", "네오"))
        friendList.add(Member(5, "url", "피치"))
        friendAdapter.submitList(friendList)
        friendAdapter.notifyDataSetChanged()

        // dummyData2
        diaryList.add(Diary(1, "춘천 한림대", "강원도 춘천시", "Happy", Thumbnail(1, "url"), "Hello", "2024-05-06", false, true))
        diaryList.add(Diary(2, "춘천 강원대", "강원도 춘천시", "Happy", Thumbnail(1, "url"), "Hello", "2024-05-06", false, true))
        diaryAdapter.submitList(diaryList)
        diaryAdapter.notifyDataSetChanged()

        binding.friendListIcon.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendsListFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.friendList.adapter = friendAdapter
        binding.timeLineRecyclerView.adapter = diaryAdapter

        binding.friendList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.timeLineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}