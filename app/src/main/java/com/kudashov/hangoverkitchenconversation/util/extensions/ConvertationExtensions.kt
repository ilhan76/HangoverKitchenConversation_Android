package com.kudashov.hangoverkitchenconversation.net.repository

import com.kudashov.hangoverkitchenconversation.*
import com.kudashov.hangoverkitchenconversation.data.*
import com.kudashov.hangoverkitchenconversation.net.response.SuccessAuthResponse

// region Auth
fun LoginQuery.Login.toSuccessAuthResponse() = SuccessAuthResponse(
    user = User(
        isActivated = this.user.isActivated,
        personalInfo = Profile(
            name = this.user.personalInfo?.name ?: "",
            description = this.user.personalInfo?.description
                ?: ""
        )
    ),
    accessToken = this.accessToken
)

fun UpdateUserMutation.UpdateProfileInfo.toProfile() = Profile(
    name = this.name ?: "",
    description = this.description ?: ""
)
// endregion

// region Room
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
// endregion

//region Messages
fun GetMessagesQuery.Message.toDomain() = Message(
    id = id,
    date = date, //todo Добавить форматирование
    author = Profile(
        name = author?.name ?: "Anonymous",
        description = author?.description ?: ""
    ),
    text = text,
    photos = photoes ?: emptyList()
)

fun ObserveNewMessageSubscription.NewMessages.toDomain() = Message(
    id = id,
    date = date, //todo Добавить форматирование
    author = Profile(
        name = author?.name ?: "Anonymous",
        description = author?.description ?: ""
    ),
    text = text,
    photos = photoes ?: emptyList()
)
//endregion