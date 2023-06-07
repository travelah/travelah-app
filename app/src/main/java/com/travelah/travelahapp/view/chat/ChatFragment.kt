package com.travelah.travelahapp.view.chat

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelah.travelahapp.R
import com.travelah.travelahapp.adapter.GroupChatAdapter
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.database.ChatItem
import com.travelah.travelahapp.data.remote.models.Chat
import com.travelah.travelahapp.data.remote.models.HistoryChat
import com.travelah.travelahapp.databinding.FragmentChatBinding
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: GroupChatAdapter

    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    private val chatViewModel: ChatViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        factory = activity?.let { ViewModelFactory.getInstance(it) }!!
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adapter = GroupChatAdapter()


        mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
            chatViewModel.getGroupChatHistory(token).observe(viewLifecycleOwner) {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
                binding.rvGroupChat.layoutManager = LinearLayoutManager(activity)
                binding.rvGroupChat.adapter = adapter
                Log.d("Success", "success add to submit data")
            }
        }
        return root
    }

    private fun setOnItemDeleteAction(token: String, groupChatId: Int) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Do you want to delete this item?")
            .setPositiveButton("Yes") { _, _ ->
                chatViewModel.deleteGroupChatById("Bearer $token", groupChatId)
                    .observe(viewLifecycleOwner) { response ->
                        if (response is Result.Success) {
                            Toast.makeText(activity, "Item succesfully deleted", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    private fun isRecyclerViewVisible(visibility: Boolean) {
        if (visibility) {
            binding.tvCondition.visibility = View.GONE
            binding.rvGroupChat.visibility = View.VISIBLE
        } else {
            binding.tvCondition.visibility = View.VISIBLE
            binding.rvGroupChat.visibility = View.GONE
        }
    }

//    private fun groupChatByDate(data: List<HistoryChat>): Map<String, List<HistoryChat>> {
//        return data.groupBy { item ->
//            val diff = calculateDateDifferenceAndFormat(item.updatedAt)
//            when {
//                diff == 0 -> "Today"
//                diff == 1 -> "Yesterday"
//                diff <= 7 -> "Previous 7 days"
//                diff <= 30 -> "Previous 30 days"
//                else -> "More than 30 days"
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun calculateDateDifferenceAndFormat(dateString: String): Int {
//        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//        val currentDate = Calendar.getInstance().time
//
//        val date = inputFormat.parse(dateString)
//        val differenceInMillis: Long = currentDate.time - date.time
//        val differenceInDays: Long = differenceInMillis / (1000 * 60 * 60 * 24)
//
//        return differenceInDays.toInt()
//    }

    //            chatViewModel.getRecentHistoryChat(token).observe(viewLifecycleOwner) { chatResult ->
//                when (chatResult) {
//                    is Result.Loading -> {
//                        binding.tvCondition.text = getString(R.string.loading)
//                        isRecyclerViewVisible(false)
//                    }
//                    is Result.Success -> {
//                        val chatList = chatResult.data
//                        if (!chatList.isEmpty()) {
//                            isRecyclerViewVisible(true)
//                            adapter.submitData(viewLifecycleOwner.lifecycle, chatList)
//                            adapter.setOnItemClickCallback(object :
//                                GroupChatAdapter.OnItemClickCallback {
//                                override fun onItemClicked(data: ChatItem) {
//                                    val intent = Intent(activity, DetailChatActivity::class.java)
//                                    startActivity(intent)
//                                }
//
//                                override fun onItemDelete(groupChatId: Int) {
//                                    setOnItemDeleteAction(token, groupChatId)
//                                }
//                            })
//                        } else {
//                            binding.tvCondition.text = getString(R.string.no_history_data)
//                            isRecyclerViewVisible(false)
//                        }
//                    }
//                    is Result.Error -> {
//                        binding.tvCondition.text = getString(R.string.failed_error)
//                        isRecyclerViewVisible(false)
//                    }
//                }
//
//            }
}