package com.example.everymoment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.everymoment.presentation.view.SearchFilterDialogFragment
import com.example.everymoment.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var filterBottomSheet: SearchFilterDialogFragment

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
        filterBottomSheet = SearchFilterDialogFragment()
    }

    private fun showFilterSheet() {
        filterBottomSheet.show(parentFragmentManager, SearchFilterDialogFragment.TAG)
    }

}