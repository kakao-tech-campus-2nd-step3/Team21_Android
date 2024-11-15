package potatocake.katecam.everymoment.presentation.view.sub.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.dto.response.Member
import potatocake.katecam.everymoment.databinding.FragmentFriendRequestBinding
import potatocake.katecam.everymoment.presentation.adapter.FriendRequestAdapter
import potatocake.katecam.everymoment.presentation.viewModel.FriendRequestViewModel
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class FriendRequestFragment : Fragment() {

    private lateinit var binding: FragmentFriendRequestBinding
    private lateinit var adapter: FriendRequestAdapter
    private val viewModel: FriendRequestViewModel by viewModels()

    private var allMembers = mutableListOf<Member>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        observeViewModel()
        viewModel.fetchMembers()


        binding.friendsBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendsListFragment())
                addToBackStack(null)
                commit()
            }
        }

    }

    private fun observeViewModel() {
        viewModel.members.observe(viewLifecycleOwner) { members ->
            allMembers.clear()
            allMembers.addAll(members)
            updateAdapterList()
        }
    }

    private fun setupRecyclerView() {
        adapter = FriendRequestAdapter(requireActivity(), viewModel)
        binding.friendRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendRequestRecyclerView.adapter = adapter
        updateAdapterList()
    }

    private var myCoroutineJob: Job = Job()
    private val myCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + myCoroutineJob
    private fun setupSearch() {

        GlobalScope.launch(context = myCoroutineContext) {
            val editTextFlow = binding.searchUserEditText.textChangesToFlow()

            editTextFlow
                .debounce(300)
                .filter { it?.length!! > 0 }
                .onEach {
                    Log.d("FriendRequestSearch", "flow로 받는다 $it")
                    filterMembers(it.toString())
                }
                .launchIn(this)
        }
    }

    private fun filterMembers(searchText: String) {
        val filteredList = allMembers.filter {
            it.nickname.contains(searchText, ignoreCase = true)
        }

        adapter.submitList(filteredList) {
            if (filteredList.isEmpty()) {
                binding.friendRequestRecyclerView.visibility = View.GONE
                binding.searchFriend.visibility = View.VISIBLE
                binding.searchFriend.setHint(R.string.search_nothing)
            } else {
                binding.friendRequestRecyclerView.visibility = View.VISIBLE
                binding.searchFriend.visibility = View.GONE
            }
        }
    }

    fun EditText.textChangesToFlow(): Flow<CharSequence?> {
        return callbackFlow {
            val listener = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    Log.d("FriendRequestSearch", "textChangesToFlow() / TextWatcher onTextChanged text : $text")
                    trySend(text)
                }
            }
            addTextChangedListener(listener)
            awaitClose {
                Log.d("FriendRequestSearch", "textChangesToFlow() awaitClose 실행")
                removeTextChangedListener(listener)
            }
        }.onStart {
            Log.d("FriendRequestSearch", "textChangesToFlow() / onStart 발동")
            emit(text)
        }
    }

    private fun updateAdapterList() {
        adapter.submitList(allMembers.toList())
    }
}