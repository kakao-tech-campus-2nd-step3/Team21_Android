package potatocake.katecam.everymoment.presentation.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import potatocake.katecam.everymoment.GlobalApplication
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.databinding.FragmentTodayLogBinding
import potatocake.katecam.everymoment.presentation.adapter.TimelineAdapter
import potatocake.katecam.everymoment.presentation.view.sub.NotificationFragment
import potatocake.katecam.everymoment.presentation.view.sub.diary.ManualDiaryEditFragment
import potatocake.katecam.everymoment.presentation.viewModel.TimelineViewModel
import potatocake.katecam.everymoment.services.location.LocationService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class TodayLogFragment : Fragment() {

    private val fineLocationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                checkCoarseLocationPermission()
            } else {
                showPermissionDeniedDialog("위치 권한")
            }
        }

    private val coarseLocationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                checkNotificationPermission()
            } else {
                showPermissionDeniedDialog("위치 권한")
            }
        }

    private val notificationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startLocationServiceIfEnabled()
            } else {
                showPermissionDeniedDialog("알림 권한")
            }
        }

    private val backgroundLocationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                checkBackgroundLocation()
            } else {
                showPermissionDeniedDialog("위치 권한")
            }
        }

    private lateinit var binding: FragmentTodayLogBinding
    private val viewModel: TimelineViewModel by viewModels()
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

        checkFineLocationPermission()

        startLocationServiceIfEnabled()

        val adapter = TimelineAdapter(requireActivity(), viewModel)
        setupRecyclerView(adapter)
        observeViewModel(adapter)

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        arguments?.getString("selected_date")?.let { selectedDate ->
            updateDate(selectedDate)
        } ?: run {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time).also {
                updateDate(it)
            }
        }

        binding.notification.setOnClickListener {
            navigateToNotificationFragment()
        }

        binding.nextDate.setOnClickListener {
            changeDate(1)
        }

        binding.prevDate.setOnClickListener {
            changeDate(-1)
        }

        binding.addDiaryButton.setOnClickListener {
            moveToManualDiary()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun observeViewModel(adapter: TimelineAdapter) {
        viewModel.diaries.observe(viewLifecycleOwner) { diaryList ->
            adapter.submitList(diaryList)
        }
    }

    private fun refreshData() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        viewModel.fetchDiaries(currentDate)
    }

    private fun changeDate(days: Int) {
        calendar.add(Calendar.DATE, days)
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        updateDate(formattedDate)
    }

    private fun updateDate(date: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        calendar.time = dateFormat.parse(date) ?: calendar.time

        viewModel.fetchDiaries(date)
        binding.currentDate.text = SimpleDateFormat("M월 d일 (E)", Locale("ko", "KR")).format(calendar.time)
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

        binding.timeLineRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (!viewModel.isLoading.value!! && totalItemCount <= (lastVisibleItemPosition + 2)) {
                    viewModel.fetchNextPage()
                }
            }
        })
    }

    private fun checkFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            fineLocationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            checkCoarseLocationPermission()
        }
    }

    private fun checkCoarseLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            coarseLocationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            checkBackgroundLocation()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                startLocationServiceIfEnabled()
            }
        } else {
            startLocationServiceIfEnabled()
        }
    }

    private fun checkBackgroundLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                backgroundLocationPermissionRequest.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                checkNotificationPermission()
            }
        }
    }

    private fun showPermissionDeniedDialog(permissionName: String) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("알림")
            .setMessage("${permissionName}이 허용되어야 앱이 실행됩니다.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = android.net.Uri.fromParts("package", requireContext().packageName, null)
                }
                startActivity(intent)
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

    private fun startLocationServiceIfEnabled() {
        val isInitialLaunch = GlobalApplication.prefs.getBoolean("isInitialLaunch", false)
        val isAutoNotificationEnabled = GlobalApplication.prefs.getBoolean("isAutoNotificationEnabled", false)

        if (isInitialLaunch || isAutoNotificationEnabled) {
            val intent = Intent(requireContext(), LocationService::class.java)
            ContextCompat.startForegroundService(requireContext(), intent)
        }

        if (isInitialLaunch) {
            GlobalApplication.prefs.setBoolean("isInitialLaunch", false)
        }
    }

    private fun moveToManualDiary(){
        val fragment = ManualDiaryEditFragment()
        val bundle = Bundle()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        bundle.putString("selected_date", binding.currentDate.text.toString())
        bundle.putString("formatted_date", formattedDate)
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }
}