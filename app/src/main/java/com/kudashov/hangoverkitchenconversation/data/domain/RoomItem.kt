package com.kudashov.hangoverkitchenconversation.data.domain

data class RoomItem(
    val id: String,
    val title: String,
    val description: String,
    val isOpen: Boolean,
    val limit: Int,
    val participantsCount: Int
)