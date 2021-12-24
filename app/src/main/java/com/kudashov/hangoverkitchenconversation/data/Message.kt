package com.kudashov.hangoverkitchenconversation.data

data class Message(
    val id: String,
    val date: String,
    val author: Profile,
    val text: String
)