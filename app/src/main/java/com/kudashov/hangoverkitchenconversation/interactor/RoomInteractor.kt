package com.kudashov.hangoverkitchenconversation.interactor

import com.kudashov.hangoverkitchenconversation.data.RoomDetail
import com.kudashov.hangoverkitchenconversation.data.RoomItem
import com.kudashov.hangoverkitchenconversation.net.repository.RoomRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class RoomInteractor(
    private val roomRepository: RoomRepository
) {

    fun createRoom(
        token: String,
        title: String,
        description: String,
        isOpen: Boolean,
        canSendAnonymousMessage: Boolean,
        limit: Int
    ): Observable<String> {
        return roomRepository.createRoom(
            token,
            title,
            description,
            isOpen,
            canSendAnonymousMessage,
            limit
        )
    }

    fun getRoom(token: String, id: String): Observable<RoomDetail> {
        return roomRepository.getRoom(token, id)
    }

    fun joinRoom(token: String, id: String): Observable<RoomDetail> {
        return roomRepository.joinRoom(token, id)
    }

    fun leaveRoom(token: String, id: String): Completable {
        return roomRepository.leaveRoom(token, id)
    }

    fun getAllRooms(token: String) : Observable<List<RoomItem>> {
        return roomRepository.getAllRooms(token)
    }

    fun getOwnRooms(token: String): Observable<List<RoomItem>> {
        return roomRepository.getOwnRooms(token)
    }

    fun getManegedRooms(token: String): Observable<List<RoomItem>> {
        return roomRepository.getManegedRooms(token)
    }
}