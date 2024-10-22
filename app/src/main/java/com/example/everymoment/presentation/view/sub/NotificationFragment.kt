package com.example.everymoment.presentation.view.sub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.everymoment.data.repository.NotificationRepository
import com.example.everymoment.databinding.FragmentNotificationBinding
import com.example.everymoment.presentation.viewModel.NotificationViewModel
import com.example.everymoment.presentation.viewModel.NotificationViewModelFactory

class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var viewModel: NotificationViewModel
    private val notificationRepository = NotificationRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, NotificationViewModelFactory(notificationRepository)).get(
            NotificationViewModel::class.java
        )
        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

}