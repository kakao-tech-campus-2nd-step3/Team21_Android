package com.example.everymoment.presentation.view.main.search

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.databinding.FragmentSearchBinding
import com.example.everymoment.presentation.adapter.FriendsListAdapter
import com.example.everymoment.presentation.adapter.SearchAdapter
import com.example.everymoment.presentation.viewModel.SearchViewModel
import com.example.everymoment.presentation.viewModel.factory.SearchViewModelFactory
import com.google.android.material.internal.ViewUtils.hideKeyboard

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var filterBottomSheet: SearchFilterDialogFragment
    private lateinit var adapter: SearchAdapter
    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(DiaryRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
        setFilterSheet()

        binding.filter.setOnClickListener {
            showFilterSheet()
        }
        binding.filterDesc.setOnClickListener {
            showFilterSheet()
        }


    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter(requireActivity(), searchViewModel)
        binding.searchedDiaryRecyclerView.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupSearchView() {
        binding.searchWindow.apply {

            imeOptions = EditorInfo.IME_ACTION_SEARCH
            inputType = InputType.TYPE_CLASS_TEXT

            setOnEditorActionListener { textView, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(textView.text.toString())
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            searchViewModel.fetchSearchedDiaries(
                keyword = query,
                emoji = null,
                category = null,
                from = null,
                until = null,
                bookmark = null
            )
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchWindow.windowToken, 0)
    }

    private fun observeViewModel() {
        searchViewModel.searchDiaries.observe(viewLifecycleOwner) { diaries ->
            adapter.submitList(diaries)
        }
    }
    private fun setFilterSheet() {
        filterBottomSheet = SearchFilterDialogFragment()
    }

    private fun showFilterSheet() {
        filterBottomSheet.show(parentFragmentManager, SearchFilterDialogFragment.TAG)
    }

}