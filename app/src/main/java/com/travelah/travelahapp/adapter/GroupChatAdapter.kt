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
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

//private fun groupChatByDate(data: List<ChatEntity>): Map<String, List<ChatEntity>> {
//    return data.groupBy { item ->
//        val diff = calculateDateDifferenceAndFormat(item.latestChatDate)
//        when {
//            diff < 1 -> "Today"
//            diff < 2 -> "Yesterday"
//            diff <= 7 -> "Previous 7 days"
//            diff <= 30 -> "Previous 30 days"
//            else -> "More than 30 days"
//        }
//    }
//}

//private fun calculateDateDifferenceAndFormat(dateString: String): Int {
//    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//    val currentDate = Calendar.getInstance().time
//
//    val date = inputFormat.parse(dateString)
//    val differenceInMillis: Long = currentDate.time - date.time
//    val differenceInDays: Long = differenceInMillis / (1000 * 60 * 60 * 24)
//
//    return differenceInDays.toInt()
//}

//class HeaderViewHolder(val binding: ItemGroupChatHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
//    fun bind(group: String) {
//        binding.tvGroupChatHeader.text = group
//    }
//}

//override fun getItemViewType(position: Int): Int {
//    var count = 0
//    for ((group, chats) in groupChatData) {
//        if (position == count) {
//            return GroupChatAdapter.ViewType.HEADER.ordinal
//        }
//        count++
//        if (position < count + chats.size) {
//            return GroupChatAdapter.ViewType.ITEM.ordinal
//        }
//        count += chats.size
//    }
//    return super.getItemViewType(position)
//}

// onBind
//var count = 0
//for ((group, messages) in groupChatData) {
//    if (position == count) {
//        (holder as HeaderViewHolder).bind(group)
//        return
//    }
//    count++
//    if (position < count + messages.size) {
//        (holder as GroupChatAdapter.GroupChatViewHolder).bind(messages[position - count])
//        return
//    }
//    count += messages.size
//}

// return viewHolder
//return when (viewType) {
//    GroupChatAdapter.ViewType.HEADER.ordinal -> {
//        val headerBinding = ItemGroupChatHeaderBinding.inflate(inflater, parent, false)
//        HeaderViewHolder(headerBinding)
//    }
//    else -> {
//        val itemBinding = ItemGroupChatBinding.inflate(inflater, parent, false)
//        GroupChatViewHolder(itemBinding)
//    }
//}


//private enum class ViewType {
//    HEADER,
//    ITEM
//}

// private val groupChatData: Map<String, List<ChatEntity>> = groupChatByDate(snapshot().items)