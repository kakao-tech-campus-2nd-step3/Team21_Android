package com.example.everymoment.presentation.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.LocationService
import com.example.everymoment.R
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.databinding.FragmentTodayLogBinding
import com.example.everymoment.presentation.adapter.TimelineAdapter
import com.example.everymoment.presentation.view.sub.NotificationFragment
import com.example.everymoment.presentation.viewModel.TimelineViewModel
import com.example.everymoment.presentation.viewModel.TimelineViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TodayLogFragment : Fragment() {

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            val notificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false
            } else {
                true
            }

            if ((fineLocationGranted || coarseLocationGranted) && notificationGranted) {
                startLocationService()
            } else {
                showPermissionDeniedDialog()
            }
        }

    private lateinit var binding: FragmentTodayLogBinding
    private lateinit var viewModel: TimelineViewModel
    private val diaryRepository = DiaryRepository()
    private val calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, TimelineViewModelFactory(diaryRepository)).get(
            TimelineViewModel::class.java
        )

        checkPermissions()
        updateDateText()

        val adapter = TimelineAdapter(requireActivity(), viewModel)
        setupRecyclerView(adapter)
        observeViewModel(adapter)

        val initialDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        viewModel.fetchDiaries(initialDate)

        binding.notification.setOnClickListener {
            navigateToNotificationFragment()
        }

        binding.nextDate.setOnClickListener {
            calendar.add(Calendar.DATE, 1)
            updateDateText()
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            viewModel.fetchDiaries(currentDate)
        }

        binding.prevDate.setOnClickListener {
            calendar.add(Calendar.DATE, -1)
            updateDateText()
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            viewModel.fetchDiaries(currentDate)
        }
    }

    private fun observeViewModel(adapter: TimelineAdapter) {
        viewModel.diaries.observe(viewLifecycleOwner) { diaryList ->
            adapter.submitList(diaryList)
        }
    }

    private fun updateDateText() {
        val formattedDate = SimpleDateFormat("M월 d일 (E)", Locale("ko", "KR")).format(calendar.time)
        binding.currentDate.text = formattedDate
    }

    private fun navigateToNotificationFragment() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, NotificationFragment())
            addToBackStack(null)
            commit()
        }
    }

    private fun setupRecyclerView(adapter: TimelineAdapter) {
        binding.timeLineRecyclerView.adapter = adapter
        binding.timeLineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getNeededPermissions(): List<String> {
        val permissionsNeeded = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        return permissionsNeeded
    }

    private fun requestPermissions(permissionsNeeded: List<String>) {
        if (permissionsNeeded.isNotEmpty()) {
            permissionRequest.launch(permissionsNeeded.toTypedArray())
        } else {
            startLocationService()
        }
    }

    private fun checkPermissions() {
        val permissionsNeeded = getNeededPermissions()
        requestPermissions(permissionsNeeded)
    }

    private fun showPermissionDeniedDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Request Permission")
            .setMessage("위치 권한과 알림 권한이 허용되어야 앱이 실행됩니다.")
            .setPositiveButton("재시도") { _, _ ->
                checkPermissions()
            }
            .setNegativeButton("종료") { _, _ ->
                requireActivity().finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun startLocationService() {
        val intent = Intent(requireContext(), LocationService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)
    }
}