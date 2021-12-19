package com.kudashov.hangoverkitchenconversation.adapters.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.kudashov.hangoverkitchenconversation.adapters.delegates.RoomItemClickDelegate
import com.kudashov.hangoverkitchenconversation.data.RoomItem
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.item_room.view.*

class RoomViewHolder(
    val view: View,
    delegate: RoomItemClickDelegate?
) : RecyclerView.ViewHolder(view) {
    private val clickableArea: View = view.clickable_area
    private val titleTv: TextView = view.tv_title
    private val descriptionTv: TextView = view.tv_description
    private val lockIv: ImageView = view.iv_is_open
    private val numberOfParticipants: TextView = view.tv_number_of_participants

    private var item: RoomItem? = null

    init {
        clickableArea.setOnClickListener {
            delegate?.toRoomDetail(item?.id)
        }
    }

    fun bind(item: RoomItem) {
        this.item = item
        titleTv.text = item.title
        descriptionTv.text = item.description
        if (item.isOpen) {
            lockIv.setImageDrawable(
                AppCompatResources.getDrawable(
                    view.context,
                    R.drawable.ic_lock_open
                )
            )
        } else {
            lockIv.setImageDrawable(
                AppCompatResources.getDrawable(
                    view.context,
                    R.drawable.ic_lock_close
                )
            )
        }
        if (item.limit != -1 && item.limit > 0){
            numberOfParticipants.text = view.context.getString(
                R.string.item_room_participant_limit_format,
                item.participantsCount,
                item.limit
            )
        } else {
            numberOfParticipants.text = view.context.getString(
                R.string.item_room_participant_no_limit_format,
                item.participantsCount
            )
        }
    }
}