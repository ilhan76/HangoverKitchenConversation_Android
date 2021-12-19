package com.kudashov.hangoverkitchenconversation.interactor

import com.kudashov.hangoverkitchenconversation.data.Message
import com.kudashov.hangoverkitchenconversation.net.repository.MessagesRepository
import com.kudashov.hangoverkitchenconversation.util.io
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class MessagesInteractor(
    private val repository: MessagesRepository
) {

    fun getMessages(token: String, roomId: String): Single<List<Message>> {
        return repository.getMessages(token, roomId).io()
    }

    fun sendMessages(
        token: String,
        roomId: String,
        text: String,
        isAnonymous: Boolean,
        photos: List<String>?
    ): Completable {
        return repository.sendMessages(token, roomId, text, isAnonymous, photos).io()
    }

    fun observeMessages(token: String, roomId: String): Observable<Message> {
        return repository.observeMessages(token, roomId).io()
    }

}