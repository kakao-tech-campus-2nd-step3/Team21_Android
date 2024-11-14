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
import potatocake.katecam.everymoment.data.repository.MyInfoRepository
import potatocake.katecam.everymoment.extensions.CustomDialog
import potatocake.katecam.everymoment.presentation.listener.OnDeleteCommentListener

class PostFragment : Fragment(), OnDeleteCommentListener {

    private lateinit var binding: FragmentPostBinding
    private lateinit var postAdapter: PostAdapter
    private val imm: InputMethodManager by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    private val postRepository: PostRepository = PostRepository()
    private val myInfoRepository: MyInfoRepository = MyInfoRepository()
    private lateinit var delCommentDialog: CustomDialog
    private var selectedFriendPosition: Int = -1

    private val viewModel: PostViewModel by viewModels {
        PostViewModelFactory(postRepository, myInfoRepository)
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
        selectedFriendPosition = arguments?.getInt("selected_friend_position", -1)!!

        (activity as? MainActivity)?.hideNavigationBar()

        setPostAdapter()
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

    fun hideCommentWindow() {
        binding.commentWindow.visibility = View.GONE
    }

    fun showCommentWindow() {
        binding.commentWindow.visibility = View.VISIBLE
        imm.hideSoftInputFromWindow(binding.comment.windowToken, 0)
    }

    private fun setViewModelObserver() {
        viewModel.post.observe(viewLifecycleOwner) {
            postAdapter.updatePost(it)
        }
        viewModel.images.observe(viewLifecycleOwner) {
            postAdapter.updateImages(it)
        }
        viewModel.comments.observe(viewLifecycleOwner) {
            postAdapter.updateComments(it.comments)
            if (it.scrollToBottom) {
                val lastPosition = postAdapter?.itemCount?.minus(1) ?: 0
                binding.recyclerView.scrollToPosition(lastPosition)
            }
        }
        viewModel.likeCnt.observe(viewLifecycleOwner) {
            postAdapter.updateLikeCnt(it)
        }
        viewModel.commentCnt.observe(viewLifecycleOwner) {
            postAdapter.updateCommentCnt(it)
        }
    }

    private fun getComments() {
        lifecycleScope.launch {
            viewModel.getCommentCnt()
            viewModel.getComments()
            viewModel.getUserId()
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
                }
            }
        })
    }

    private fun setClickListeners() {
        binding.backButton.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("selected_friend_position", selectedFriendPosition)
            }
            requireActivity().supportFragmentManager.setFragmentResult("selected_position", bundle)
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.commentBtn.setOnClickListener {
            val comment = binding.comment.text.toString()
            if (comment.isNotBlank()) {
                viewModel.postComment(comment)
                imm.hideSoftInputFromWindow(binding.comment.windowToken, 0)
                binding.comment.clearFocus()
                binding.comment.setText("")
            }
        }
    }

    private fun setPostAdapter() {
        postAdapter = PostAdapter(this, this, viewModel)
        binding.recyclerView.adapter = postAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDeleteCommentRequested(commentId: Int) {
        delCommentDialog = CustomDialog(
            message = resources.getString(R.string.del_comment),
            negText = resources.getString(R.string.cancel),
            posText = resources.getString(R.string.delete),
            onPositiveClick = {
                viewModel.delComment(commentId)
            }
        )
        delCommentDialog.show(parentFragmentManager, "delCommentDialog")
    }

}