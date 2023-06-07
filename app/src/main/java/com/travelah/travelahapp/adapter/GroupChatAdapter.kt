package com.travelah.travelahapp.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.travelah.travelahapp.data.database.ChatItem
import com.travelah.travelahapp.data.remote.models.Chat
import com.travelah.travelahapp.data.remote.models.HistoryChat
import com.travelah.travelahapp.databinding.ItemGroupChatBinding
import com.travelah.travelahapp.databinding.ItemGroupChatHeaderBinding
import com.travelah.travelahapp.utils.withDateFormatFromISO
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

class GroupChatAdapter: PagingDataAdapter<ChatItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
//    private val groupChatData: Map<String, List<ChatItem>> = groupChatByDate(snapshot().items)
    private var onItemClickCallback: OnItemClickCallback? = null

    private enum class ViewType {
        HEADER,
        ITEM
    }

    private fun groupChatByDate(data: List<ChatItem>): Map<String, List<ChatItem>> {
        return data.groupBy { item ->
            val diff = calculateDateDifferenceAndFormat(item.latestChatDate)
            when {
                diff == 0 -> "Today"
                diff == 1 -> "Yesterday"
                diff <= 7 -> "Previous 7 days"
                diff <= 30 -> "Previous 30 days"
                else -> "More than 30 days"
            }
        }
    }

    private fun calculateDateDifferenceAndFormat(dateString: String): Int {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val currentDate = Calendar.getInstance().time

        val date = inputFormat.parse(dateString)
        val differenceInMillis: Long = currentDate.time - date.time
        val differenceInDays: Long = differenceInMillis / (1000 * 60 * 60 * 24)

        return differenceInDays.toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
//        return when (viewType) {
//            ViewType.HEADER.ordinal -> {
//                val headerBinding = ItemGroupChatHeaderBinding.inflate(inflater, parent, false)
//                HeaderViewHolder(headerBinding)
//            }
//            else -> {
//                val itemBinding = ItemGroupChatBinding.inflate(inflater, parent, false)
//                GroupChatViewHolder(itemBinding)
//            }
//        }
        val itemBinding = ItemGroupChatBinding.inflate(inflater, parent, false)
        return GroupChatViewHolder(itemBinding)
    }

//    override fun getItemCount(): Int = listGroupChat.values.flatten().size + listGroupChat.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        var count = 0
//        for ((group, messages) in groupChatData) {
//            if (position == count) {
//                (holder as HeaderViewHolder).bind(group)
//                return
//            }
//            count++
//            if (position < count + messages.size) {
//                (holder as GroupChatViewHolder).bind(messages[position - count])
//                return
//            }
//            count += messages.size
//        }
        val data = getItem(position)
        data?.let { (holder as GroupChatViewHolder).bind(it) }
    }

//    override fun getItemViewType(position: Int): Int {
//        var count = 0
//        for ((group, messages) in groupChatData) {
//            if (position == count) {
//                return ViewType.HEADER.ordinal
//            }
//            count++
//            if (position < count + messages.size) {
//                return ViewType.ITEM.ordinal
//            }
//            count += messages.size
//        }
//        return super.getItemViewType(position)
//    }

//    class HeaderViewHolder(val binding: ItemGroupChatHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(group: String) {
//            binding.tvGroupChatHeader.text = group
//        }
//    }

    inner class GroupChatViewHolder(val binding: ItemGroupChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatItem) {
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
        fun onItemClicked(data: ChatItem)
        fun onItemDelete(groupChatId: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}