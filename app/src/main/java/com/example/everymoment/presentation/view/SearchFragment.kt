package com.example.everymoment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.everymoment.R
import com.example.everymoment.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFilterSheet()

        binding.filter.setOnClickListener {
            showFilterSheet()
        }
        binding.filterDesc.setOnClickListener {
            showFilterSheet()
        }
    }

    private fun setFilterSheet() {

    }

    private fun showFilterSheet() {

    }

}