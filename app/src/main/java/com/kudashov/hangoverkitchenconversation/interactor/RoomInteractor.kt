package com.kudashov.hangoverkitchenconversation.interactor

import com.kudashov.hangoverkitchenconversation.data.RoomDetail
import com.kudashov.hangoverkitchenconversation.data.RoomItem
import com.kudashov.hangoverkitchenconversation.net.repository.RoomRepository
import com.kudashov.hangoverkitchenconversation.util.io
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

    fun isUserMemberedInRoom(token: String, id: String): Observable<Boolean> {
        return roomRepository.isUserMemberedInRoom(token, id).io()
    }

    fun joinRoom(token: String, id: String): Observable<RoomDetail> {
        return roomRepository.joinRoom(token, id).io()
    }

    fun leaveRoom(token: String, id: String): Completable {
        return roomRepository.leaveRoom(token, id).io()
    }

    fun getAllRooms(token: String) : Observable<List<RoomItem>> {
        return roomRepository.getAllRooms(token).io()
    }

    fun getOwnRooms(token: String): Observable<List<RoomItem>> {
        return roomRepository.getOwnRooms(token).io()
    }

    fun getManegedRooms(token: String): Observable<List<RoomItem>> {
        return roomRepository.getManegedRooms(token).io()
    }
}