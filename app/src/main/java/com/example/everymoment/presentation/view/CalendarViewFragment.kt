package com.example.everymoment.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.everymoment.R
import com.example.everymoment.databinding.FragmentCalendarViewBinding
import com.kakao.sdk.common.util.Utility

class CalendarViewFragment : Fragment() {

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
    }

}