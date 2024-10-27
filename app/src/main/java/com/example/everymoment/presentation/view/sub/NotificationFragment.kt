package com.example.everymoment.presentation.view.sub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.repository.NotificationRepository
import com.example.everymoment.databinding.FragmentNotificationBinding
import com.example.everymoment.presentation.adapter.NotificationAdapter
import com.example.everymoment.presentation.adapter.TimelineAdapter
import com.example.everymoment.presentation.viewModel.NotificationViewModel
import com.example.everymoment.presentation.viewModel.NotificationViewModelFactory

class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private val notificationRepository = NotificationRepository()
    private val friendRepository = FriendRepository()
    private val viewModel: NotificationViewModel by viewModels {
        NotificationViewModelFactory(notificationRepository, friendRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val adapter = NotificationAdapter(viewModel)
        setupRecyclerView(adapter)
        observeViewModel(adapter)
        viewModel.fetchNotifications()
    }
    private fun setupRecyclerView(adapter: NotificationAdapter) {
        binding.notificationRecyclerView.adapter = adapter
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel(adapter: NotificationAdapter) {
        viewModel.notifications.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }
}