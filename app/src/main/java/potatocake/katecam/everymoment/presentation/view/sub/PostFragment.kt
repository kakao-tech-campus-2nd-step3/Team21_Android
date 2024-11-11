package potatocake.katecam.everymoment.presentation.view.sub

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.repository.PostRepository
import potatocake.katecam.everymoment.databinding.FragmentPostBinding
import potatocake.katecam.everymoment.presentation.adapter.PostAdapter
import potatocake.katecam.everymoment.presentation.view.main.MainActivity
import potatocake.katecam.everymoment.presentation.viewModel.PostViewModel
import potatocake.katecam.everymoment.presentation.viewModel.factory.PostViewModelFactory
import kotlinx.coroutines.launch

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var imm: InputMethodManager
    private val postRepository: PostRepository = PostRepository()

    private val viewModel: PostViewModel by viewModels {
        PostViewModelFactory(postRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("postFragment", "${arguments?.getInt("diary_id")}")
        val diaryId = arguments?.getInt("diary_id")
        val selectedFriendName = arguments?.getString("selected_friend_name", "")

        (activity as? MainActivity)?.hideNavigationBar()

        setPostAdapter()
        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.instruction.text =
            resources.getString(R.string.post_instruction, selectedFriendName)

        setClickListeners()
        getFriendDiaryInDetail(diaryId)
        getFiles(diaryId)
        getComments()
        setViewModelObserver()
        setScrollListener()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showNavigationBar()
    }

    private fun setViewModelObserver() {
        viewModel.post.observe(viewLifecycleOwner) {
            postAdapter.updatePost(it)
        }
        viewModel.images.observe(viewLifecycleOwner) {
            postAdapter.updateImages(it)
        }
        viewModel.comments.observe(viewLifecycleOwner) {
            postAdapter.updateComments(it)
        }
        viewModel.likeCnt.observe(viewLifecycleOwner) {
            postAdapter.updateLikeCnt(it)
        }
    }

    private fun getComments() {
        lifecycleScope.launch {
            viewModel.getComments()
        }
    }

    private fun getFriendDiaryInDetail(diaryId: Int?) {
        lifecycleScope.launch {
            viewModel.getFriendDiaryinDetail(diaryId)
        }
    }

    private fun getFiles(diaryId: Int?) {
        lifecycleScope.launch {
            viewModel.getFiles(diaryId)
        }
    }

    private fun setScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
                val lastVisibleItemPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
                val totalItemCount = layoutManager?.itemCount ?: 0

                // 스크롤 방향이 아래로 내려가는 경우 (dy > 0)
                if (dy > 0 && lastVisibleItemPosition >= totalItemCount - 1) {
                    viewModel.loadNextComments()
                }
                // 스크롤 방향이 위로 올라가는 경우 (dy < 0)
                else if (dy < 0 && firstVisibleItemPosition == 0) {
                    viewModel.loadPreviousComments()
                }
            }
        })
    }

    private fun setClickListeners() {
        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.commentBtn.setOnClickListener {
            val comment = binding.comment.text.toString()
            if (comment.isNotBlank()) {
                viewModel.postComment(comment)
                imm.hideSoftInputFromWindow(binding.comment.windowToken, 0)
                binding.comment.clearFocus()
                binding.comment.setText("")
                val lastPosition = postAdapter?.itemCount?.minus(1) ?: 0
                binding.recyclerView.scrollToPosition(lastPosition)
            }
        }
    }

    private fun setPostAdapter() {
        postAdapter = PostAdapter(requireContext(), viewModel)
        binding.recyclerView.adapter = postAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}