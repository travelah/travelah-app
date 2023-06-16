package com.travelah.travelahapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.travelah.travelahapp.data.local.entity.ChatEntity
import com.travelah.travelahapp.databinding.ItemGroupChatBinding
import com.travelah.travelahapp.databinding.ItemGroupChatHeaderBinding
import com.travelah.travelahapp.utils.withDateFormatFromISO
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class GroupChatAdapter: PagingDataAdapter<ChatEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemGroupChatBinding.inflate(inflater, parent, false)
        return GroupChatViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) (holder as GroupChatViewHolder).bind(data)
    }

    inner class GroupChatViewHolder(val binding: ItemGroupChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatEntity) {
            binding.apply {
                tvLatestChat.text = chat.latestChat
                tvLatestChatDate.text = chat.latestChatDate.withDateFormatFromISO()
            }
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(chat)
            }
            binding.buttonDeleteGroupChat.setOnClickListener {
                onItemClickCallback?.onItemDelete(chat.id)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ChatEntity)
        fun onItemDelete(groupChatId: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatEntity>() {
            override fun areItemsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}