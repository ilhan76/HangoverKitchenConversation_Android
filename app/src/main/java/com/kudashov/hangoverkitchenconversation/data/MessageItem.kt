package com.kudashov.hangoverkitchenconversation.data

data class MessageItem (
    val date: String,
    val name: String,
    val text: String,
    val isMyMessage: Boolean
)