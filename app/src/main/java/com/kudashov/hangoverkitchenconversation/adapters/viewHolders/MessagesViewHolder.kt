package com.kudashov.hangoverkitchenconversation.adapters.viewHolders

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kudashov.hangoverkitchenconversation.data.MessageItem
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.item_message.view.*

class MessagesViewHolder(
    val view: View
) : RecyclerView.ViewHolder(view) {
    private val messageContainer: ViewGroup = view.message_view
    private val tvName: TextView = view.tv_name
    private val tvDate: TextView = view.tv_date
    private val tvMessageContent = view.tv_message_content

    fun bind(message: MessageItem) {
        //todo добавить изменение гравити
        val par = messageContainer.layoutParams as FrameLayout.LayoutParams
        if (message.isMyMessage) {
            par.gravity = Gravity.END
            messageContainer.layoutParams = par
            messageContainer.background = AppCompatResources.getDrawable(
                view.context, R.drawable.shape_my_message
            )
        } else {
            par.gravity = Gravity.START
            messageContainer.layoutParams = par
            messageContainer.background = AppCompatResources.getDrawable(
                view.context, R.drawable.shape_message
            )
        }
        tvName.text = message.name
        tvDate.text = message.date
        tvMessageContent.text = message.text
    }
}