package com.kudashov.hangoverkitchenconversation.net.repository

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.kudashov.hangoverkitchenconversation.*
import com.kudashov.hangoverkitchenconversation.data.RoomDetail
import com.kudashov.hangoverkitchenconversation.data.RoomItem
import com.kudashov.hangoverkitchenconversation.net.NetworkService
import com.kudashov.hangoverkitchenconversation.util.FailToJoinRoom
import com.kudashov.hangoverkitchenconversation.util.FailToLeaveRoom
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class RoomRepository {

    private val tag: String = this.javaClass.simpleName

    fun createRoom(
        token: String,
        title: String,
        description: String,
        isOpen: Boolean,
        canSendAnonymousMessage: Boolean,
        limit: Int
    ): Observable<String> {
        val subject = PublishSubject.create<String>()
        val mutation = CreateRoomMutation(
            title, description, isOpen, canSendAnonymousMessage, limit
        )

        NetworkService
            .getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.mutate(mutation)
            ?.enqueue(object : ApolloCall.Callback<CreateRoomMutation.Data>() {
                override fun onResponse(response: Response<CreateRoomMutation.Data>) {
                    val result = response.data
                    Log.d(tag, "onResponse: $result")

                    if (!response.hasErrors()) {
                        subject.onNext(response.data?.createRoom?.id!!)
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, subject)
                }
            })

        return subject
    }

    fun getRoom(token: String, id: String): Observable<RoomDetail> {
        val subject = PublishSubject.create<RoomDetail>()
        val query = GetRoomQuery(id)

        NetworkService
            .getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.query(query)
            ?.enqueue(object : ApolloCall.Callback<GetRoomQuery.Data>() {
                override fun onResponse(response: Response<GetRoomQuery.Data>) {
                    val result = response.data
                    Log.d(tag, "onResponse: $result")

                    if (!response.hasErrors()) {
                        subject.onNext(response.data?.room?.toDomain())
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, subject)
                }
            })

        return subject
    }

    fun joinRoom(token: String, id: String): Observable<RoomDetail> {
        val subject = PublishSubject.create<RoomDetail>()

        NetworkService.getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.mutate(JoinRoomMutation(id))
            ?.enqueue(object : ApolloCall.Callback<JoinRoomMutation.Data>() {
                override fun onResponse(response: Response<JoinRoomMutation.Data>) {
                    val result = response.data
                    Log.d(tag, "onResponse: $result")

                    if (!response.hasErrors()) {
                        subject.onNext(response.data?.joinRoom?.toDomain())
                    } else {
                        subject.onError(FailToJoinRoom())
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, subject)
                }

            })

        return subject
    }

    fun leaveRoom(token: String, id: String): Completable {
        val subject = PublishSubject.create<Unit>()

        NetworkService
            .getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.mutate(LeaveRoomMutation(id))
            ?.enqueue(object : ApolloCall.Callback<LeaveRoomMutation.Data>() {
                override fun onResponse(response: Response<LeaveRoomMutation.Data>) {
                    val result = response.data
                    Log.d(tag, "onResponse: $result")

                    if (!response.hasErrors()) {
                        if (response.data?.leaveRoom == true) {
                            subject.onComplete()
                        } else {
                            subject.onError(FailToLeaveRoom())
                        }
                    } else {
                        subject.onError(FailToJoinRoom())
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, subject)
                }

            })

        return Completable.fromObservable(subject)
    }

    fun getAllRooms(token: String) : Observable<List<RoomItem>> {
        val subject = PublishSubject.create<List<RoomItem>>()

        NetworkService
            .getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.query(GetAllRoomsQuery())
            ?.enqueue(object : ApolloCall.Callback<GetAllRoomsQuery.Data>() {
                override fun onResponse(response: Response<GetAllRoomsQuery.Data>) {
                    val result = response.data
                    Log.d(tag, "onResponse: $result")

                    if (!response.hasErrors()) {
                        subject.onNext(result?.allRooms?.map { it.toDomain() })
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, subject)
                }

            })

        return subject
    }

    fun getOwnRooms(token: String): Observable<List<RoomItem>> {
        val subject = PublishSubject.create<List<RoomItem>>()

        NetworkService
            .getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.query(GetOwnRoomQuery())
            ?.enqueue(object : ApolloCall.Callback<GetOwnRoomQuery.Data>() {
                override fun onResponse(response: Response<GetOwnRoomQuery.Data>) {
                    val result = response.data
                    Log.d(tag, "onResponse: $result")

                    if (!response.hasErrors()) {
                        subject.onNext(result?.ownRooms?.map { it.toDomain() })
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, subject)
                }

            })

        return subject
    }

    fun getManegedRooms(token: String): Observable<List<RoomItem>> {
        val subject = PublishSubject.create<List<RoomItem>>()

        NetworkService
            .getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.query(GetManagedRoomQuery())
            ?.enqueue(object : ApolloCall.Callback<GetManagedRoomQuery.Data>() {
                override fun onResponse(response: Response<GetManagedRoomQuery.Data>) {
                    val result = response.data
                    Log.d(tag, "onResponse: $result")

                    if (!response.hasErrors()) {
                        subject.onNext(result?.managedRooms?.map { it.toDomain() })
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, subject)
                }

            })

        return subject
    }

    private fun<T> handleError(e: ApolloException, hub: Subject<T>){
        Log.d(tag, "onFailure: $e")
        hub.onError(e)
    }
}