package com.kudashov.hangoverkitchenconversation.data

data class RoomDetail(
    val id: String,
    val title: String,
    val isOpen: Boolean,
    val date: String,
    val description: String,
    val canSendAnonimusMessage: Boolean,
    val limit: Int,
    val participantsCount: Int,
    val users: List<Profile>
) {
    companion object {
        const val BASE_LIMIT = -1 // no limit
        const val BASE_PARTICIPANT_COUNT = -1 //no info
    }
}