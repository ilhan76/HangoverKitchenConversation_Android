package com.kudashov.hangoverkitchenconversation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kudashov.hangoverkitchenconversation.adapters.viewHolders.MessagesViewHolder
import com.kudashov.hangoverkitchenconversation.data.Message
import com.kudashov.hangoverkitchenconversation.data.MessageItem
import com.kudashov.hangoverkitchenconversation_android.R

class MessagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<MessageItem> = ArrayList()

    fun setList(newList: List<MessageItem>) {
        list.clear()
        list.addAll(newList)

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessagesViewHolder(
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MessagesViewHolder) holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}