package com.kudashov.hangoverkitchenconversation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kudashov.hangoverkitchenconversation.adapters.delegates.RoomItemClickDelegate
import com.kudashov.hangoverkitchenconversation.adapters.viewHolders.RoomViewHolder
import com.kudashov.hangoverkitchenconversation.data.RoomItem
import com.kudashov.hangoverkitchenconversation_android.R

class RoomsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<RoomItem> = ArrayList()
    private var delegate: RoomItemClickDelegate? = null

    fun setList(newList: List<RoomItem>) {
        list.clear()
        list.addAll(newList)

        notifyDataSetChanged()
    }

    fun attachDelegate(newDelegate: RoomItemClickDelegate) {
        delegate = newDelegate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoomViewHolder(
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false),
            delegate = delegate
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RoomViewHolder) holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}