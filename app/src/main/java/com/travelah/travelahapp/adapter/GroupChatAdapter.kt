package com.travelah.travelahapp.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travelah.travelahapp.data.remote.models.Chat
import com.travelah.travelahapp.data.remote.models.HistoryChat
import com.travelah.travelahapp.databinding.ItemGroupChatBinding
import com.travelah.travelahapp.databinding.ItemGroupChatHeaderBinding
import com.travelah.travelahapp.utils.withDateFormatFromISO
import kotlinx.parcelize.Parcelize

class GroupChatAdapter(private val listGroupChat: Map<String, List<HistoryChat>>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onItemLongClickCallback: OnItemLongClickCallback? = null

    fun setOnItemLongClickCallback(onItemLongClickCallback: OnItemLongClickCallback) {
        this.onItemLongClickCallback = onItemLongClickCallback
    }

    interface OnItemLongClickCallback {
        fun onItemLongClicked(data: Chat)
    }

    private enum class ViewType {
        HEADER,
        ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.HEADER.ordinal -> {
                val headerBinding = ItemGroupChatHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(headerBinding)
            }
            else -> {
                val itemBinding = ItemGroupChatBinding.inflate(inflater, parent, false)
                GroupChatViewHolder(itemBinding)
            }
        }
    }

    override fun getItemCount(): Int = listGroupChat.values.flatten().size + listGroupChat.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var count = 0
        for ((group, messages) in listGroupChat) {
            if (position == count) {
                (holder as HeaderViewHolder).bind(group)
                return
            }
            count++
            if (position < count + messages.size) {
                (holder as GroupChatViewHolder).bind(messages[position - count])
                return
            }
            count += messages.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        var count = 0
        for ((group, messages) in listGroupChat) {
            if (position == count) {
                return ViewType.HEADER.ordinal
            }
            count++
            if (position < count + messages.size) {
                return ViewType.ITEM.ordinal
            }
            count += messages.size
        }
        return super.getItemViewType(position)
    }

    class HeaderViewHolder(val binding: ItemGroupChatHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(group: String) {
            binding.tvGroupChatHeader.text = group
        }
    }

    inner class GroupChatViewHolder(val binding: ItemGroupChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(groupChatItem: HistoryChat) {
            binding.apply {
                tvLatestChat.text = groupChatItem.chats[0].response
                tvLatestChatDate.text = groupChatItem.chats[0].updatedAt.withDateFormatFromISO()
            }
            binding.root.setOnLongClickListener {
                onItemLongClickCallback?.onItemLongClicked(groupChatItem.chats[0])
                true
            }
        }
    }
}

@Parcelize
data class GroupChatItem(
    val latestChat: String,
    val latestChatDate: String
): Parcelable