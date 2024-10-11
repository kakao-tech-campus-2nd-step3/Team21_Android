package com.example.everymoment.presentation.view

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.LocationService
import com.example.everymoment.R
import com.example.everymoment.data.model.Timeline
import com.example.everymoment.databinding.FragmentTodayLogBinding
import com.example.everymoment.presentation.adapter.TimelineAdapter

class TodayLogFragment : Fragment() {

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timelineList: MutableList<Timeline> = mutableListOf()
        // 리스트 예시
        timelineList.add(Timeline("오전 10:00", "빽다방 강원대점", "강원도 춘천시 충열로", "😢", true))
        timelineList.add(Timeline("오후 12:00", "천지관", "강원도 춘천시 충열로", "😢", false))

        val TodayDate = arguments?.getString("selected_date")
        Log.d("TodayDate", "Selected date: $TodayDate")

        checkPermissions()
        setupRecyclerView(timelineList)
        binding.notification.setOnClickListener {
            navigateToNotificationFragment()
        }
    }

    private fun navigateToNotificationFragment() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, NotificationFragment())
            addToBackStack(null)
            commit()
        }
    }

    private fun setupRecyclerView(timelineList: MutableList<Timeline>) {
        binding.timeLineRecyclerView.adapter = TimelineAdapter(timelineList)
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