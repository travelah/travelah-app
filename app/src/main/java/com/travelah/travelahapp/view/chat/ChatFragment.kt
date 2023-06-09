package com.travelah.travelahapp.view.chat

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelah.travelahapp.adapter.GroupChatAdapter
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.local.entity.ChatEntity
import com.travelah.travelahapp.databinding.FragmentChatBinding
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel
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
        factory = requireActivity().let { ViewModelFactory.getInstance(it) }
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        binding.rvGroupChat.layoutManager = layoutManager
        adapter = GroupChatAdapter()
        binding.rvGroupChat.adapter = adapter

        mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
            chatViewModel.getGroupChatHistory("Bearer $token").observe(viewLifecycleOwner) {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
                adapter.setOnItemClickCallback(object : GroupChatAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ChatEntity) {
                        val intent = Intent(requireActivity(), DetailChatActivity::class.java)
                        intent.putExtra(DetailChatActivity.EXTRA_ID, data.id)
                        startActivity(intent)
                    }

                    override fun onItemDelete(groupChatId: Int) {
                        setOnItemDeleteAction(token, groupChatId, it)
                    }

                })
            }
        }
    }

    private fun setOnItemDeleteAction(
        token: String,
        groupChatId: Int,
        data: PagingData<ChatEntity>
    ) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Do you want to delete this item?")
            .setPositiveButton("Yes") { _, _ ->
                chatViewModel.deleteGroupChatById("Bearer $token", groupChatId)
                    .observe(viewLifecycleOwner) { response ->
                        if (response is Result.Success) {
                            Toast.makeText(activity, "Item succesfully deleted", Toast.LENGTH_SHORT)
                                .show()
                            adapter.refresh()
                        }
                    }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}