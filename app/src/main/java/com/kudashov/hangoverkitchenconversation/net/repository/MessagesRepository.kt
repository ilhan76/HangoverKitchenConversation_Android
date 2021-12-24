package com.kudashov.hangoverkitchenconversation.net.repository

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloSubscriptionCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.kudashov.hangoverkitchenconversation.GetMessagesQuery
import com.kudashov.hangoverkitchenconversation.ObserveNewMessageSubscription
import com.kudashov.hangoverkitchenconversation.SendMessageMutation
import com.kudashov.hangoverkitchenconversation.data.Message
import com.kudashov.hangoverkitchenconversation.net.NetworkService
import com.kudashov.hangoverkitchenconversation.type.SendMessageInput
import com.kudashov.hangoverkitchenconversation.util.CustomException
import com.kudashov.hangoverkitchenconversation.util.logDebug
import com.kudashov.hangoverkitchenconversation.util.logError
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class MessagesRepository {

    fun getMessages(token: String, roomId: String): Single<List<Message>> {
        val hub = PublishSubject.create<List<Message>>()
        val query = GetMessagesQuery(roomId)

        NetworkService
            .getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.query(query)
            ?.enqueue(object : ApolloCall.Callback<GetMessagesQuery.Data>() {
                override fun onResponse(response: Response<GetMessagesQuery.Data>) {
                    logDebug("onSuccess ${response.data}")

                    if (!response.hasErrors()) {
                        hub.onNext(response.data?.messages?.map { it.toDomain() })
                    } else {
                        response.errors?.forEach {
                            logError("onResponseError: $it")
                        }
                        hub.onError(CustomException(response.errors?.first()?.message))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, hub)
                }

            })

        return Single.fromObservable(hub)
    }

    fun sendMessages(
        token: String,
        roomId: String,
        text: String,
        isAnonymous: Boolean
    ): Completable {
        val hub = PublishSubject.create<List<Message>>()
        val mutation = SendMessageMutation(
            SendMessageInput(
                roomId = roomId,
                text = text,
                isAnonimus = isAnonymous
            )
        )

        NetworkService
            .getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.mutate(mutation)
            ?.enqueue(object : ApolloCall.Callback<SendMessageMutation.Data>() {
                override fun onResponse(response: Response<SendMessageMutation.Data>) {
                    logDebug("onSuccess ${response.data}")

                    if (!response.hasErrors()) {
                        hub.onComplete()
                    } else {
                        response.errors?.forEach {
                            logError("onResponseError: $it")
                        }
                        hub.onError(CustomException(response.errors?.first()?.message))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, hub)
                }

            })

        return Completable.fromObservable(hub)
    }

    fun observeMessages(token: String, roomId: String): Observable<Message> {
        val hub = PublishSubject.create<Message>()
        val subscription = ObserveNewMessageSubscription(roomId)

        NetworkService
            .getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.subscribe(subscription)
            ?.execute(object : ApolloSubscriptionCall.Callback<ObserveNewMessageSubscription.Data> {
                override fun onResponse(response: Response<ObserveNewMessageSubscription.Data>) {
                    logDebug("onSuccess ${response.data}")

                    if (!response.hasErrors()) {
                        hub.onNext(response.data?.newMessages?.toDomain())
                    } else {
                        response.errors?.forEach {
                            logError("onResponseError: $it")
                        }
                        hub.onError(CustomException(response.errors?.first()?.message))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, hub)
                }

                override fun onCompleted() {}
                override fun onTerminated() {}
                override fun onConnected() {}
            })

        return hub
    }

    private fun <T> handleError(e: ApolloException, hub: Subject<T>) {
        logError("onFailure: $e")
        hub.onError(e)
        throw e
    }
}