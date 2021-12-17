package com.kudashov.hangoverkitchenconversation.net.repository

import com.kudashov.hangoverkitchenconversation.*
import com.kudashov.hangoverkitchenconversation.data.domain.Profile
import com.kudashov.hangoverkitchenconversation.data.domain.RoomDetail
import com.kudashov.hangoverkitchenconversation.data.domain.RoomItem

fun GetRoomQuery.Room.toDomain() = RoomDetail(
    id = id,
    title = title,
    isOpen = isOpen,
    date = date ?: "",
    description = description ?: "",
    canSendAnonimusMessage = canSendAnonimusMessage ?: false,
    limit = limit ?: RoomDetail.BASE_LIMIT,
    participantsCount = participantsCount ?: RoomDetail.BASE_PARTICIPANT_COUNT,
    users = (users?.forEach {
        Profile(
            name = it.name ?: "",
            description = it.description ?: ""
        )
    } ?: emptyList<Profile>()) as List<Profile>)

fun JoinRoomMutation.JoinRoom.toDomain() = RoomDetail(
    id = id,
    title = title,
    isOpen = isOpen,
    date = date ?: "",
    description = description ?: "",
    canSendAnonimusMessage = canSendAnonimusMessage ?: false,
    limit = limit ?: RoomDetail.BASE_LIMIT,
    participantsCount = participantsCount ?: RoomDetail.BASE_PARTICIPANT_COUNT,
    users = (users?.forEach {
        Profile(
            name = it.name ?: "",
            description = it.description ?: ""
        )
    } ?: emptyList<Profile>()) as List<Profile>)

fun GetOwnRoomQuery.OwnRoom.toDomain() = RoomItem(
    id = id,
    title = title,
    description = description ?: "",
    isOpen = isOpen,
    limit = limit ?: RoomDetail.BASE_LIMIT,
    participantsCount = participantsCount ?: RoomDetail.BASE_PARTICIPANT_COUNT
)

fun GetAllRoomsQuery.AllRoom.toDomain() = RoomItem(
    id = id,
    title = title,
    description = description ?: "",
    isOpen = isOpen,
    limit = limit ?: RoomDetail.BASE_LIMIT,
    participantsCount = participantsCount ?: RoomDetail.BASE_PARTICIPANT_COUNT
)

fun GetManagedRoomQuery.ManagedRoom.toDomain() = RoomItem(
    id = id,
    title = title,
    description = description ?: "",
    isOpen = isOpen,
    limit = limit ?: RoomDetail.BASE_LIMIT,
    participantsCount = participantsCount ?: RoomDetail.BASE_PARTICIPANT_COUNT
)

