package com.example.everymoment.presentation.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.R
import com.example.everymoment.databinding.FragmentPostBinding
import com.example.everymoment.presentation.adapter.PostAdapter

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var imm: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPostAdapter()
        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.commentBtn.setOnClickListener {
            if (binding.comment.text.toString().isNotBlank()) {
                postAdapter.addComment(binding.comment.text.toString())
                postAdapter.notifyItemInserted(postAdapter.itemCount - 1)
                imm.hideSoftInputFromWindow(binding.comment.windowToken, 0)
                binding.comment.clearFocus()
                binding.comment.setText("")
                binding.recyclerView.scrollToPosition(postAdapter.itemCount - 1)
            }
        }
    }

    private fun setPostAdapter() {
        postAdapter = PostAdapter(requireContext())
        binding.recyclerView.adapter = postAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}