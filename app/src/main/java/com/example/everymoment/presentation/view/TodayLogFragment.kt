package com.example.everymoment.presentation.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.LocationService
import com.example.everymoment.R
import com.example.everymoment.data.model.Timeline
import com.example.everymoment.databinding.FragmentTodayLogBinding
import com.example.everymoment.presentation.adapter.TimelineAdapter

class TodayLogFragment : Fragment() {
    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 2000
    }

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                startLocationService()
            } else {
                Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
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

        binding.timeLineRecyclerView.adapter = TimelineAdapter(timelineList)
        binding.timeLineRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.notification.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, NotificationFragment())
                addToBackStack(null)
                commit()
            }
        }

        checkNotificationPermission()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
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

        if (permissionsNeeded.isNotEmpty()) {
            locationPermissionRequest.launch(permissionsNeeded.toTypedArray())
        } else {
            startLocationService()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun startLocationService() {
        val intent = Intent(requireContext(), LocationService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)
    }
}