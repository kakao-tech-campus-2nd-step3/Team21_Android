package com.example.everymoment.presentation.view.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.everymoment.R
import com.example.everymoment.databinding.FragmentCalendarViewBinding
import com.example.everymoment.presentation.view.sub.NotificationFragment
import com.kakao.sdk.common.util.Utility
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import org.threeten.bp.format.DateTimeFormatter

class CalendarViewFragment : Fragment(), OnDateSelectedListener {

    private lateinit var binding: FragmentCalendarViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarViewBinding.inflate(inflater, container, false)
        Log.d("testt", "keyhash : ${Utility.getKeyHash(requireContext())}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarAlarm.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, NotificationFragment())
                addToBackStack(null)
                commit()
            }
        }

        binding.calendarView.setOnDateChangedListener(this)
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        if (selected) {
            // 선택된 날짜를 원하는 형식으로 포맷팅
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = date.date.format(formatter)

            // TodayLogFragment로 이동하면서 선택한 날짜 전달
            val todayLogFragment = TodayLogFragment().apply {
                arguments = Bundle().apply {
                    putString("selected_date", formattedDate)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, todayLogFragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}