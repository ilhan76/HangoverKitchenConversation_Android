package com.kudashov.hangoverkitchenconversation.net.repository

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.kudashov.hangoverkitchenconversation.LoginQuery
import com.kudashov.hangoverkitchenconversation.RegisterUserMutation
import com.kudashov.hangoverkitchenconversation.data.dto.ProfileDto
import com.kudashov.hangoverkitchenconversation.data.dto.UserDto
import com.kudashov.hangoverkitchenconversation.net.NetworkService
import com.kudashov.hangoverkitchenconversation.net.response.SuccessAuthResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class AuthRepository {

    fun login(email: String, pass: String): Observable<SuccessAuthResponse> {
        val subject = PublishSubject.create<SuccessAuthResponse>()
        val loginQuery = LoginQuery(email, pass)

        NetworkService
            .getInstance()
            ?.getApolloClient()
            ?.query(loginQuery)
            ?.enqueue(object : ApolloCall.Callback<LoginQuery.Data>() {
                override fun onResponse(response: Response<LoginQuery.Data>) {
                    if (!response.hasErrors()) {
                        subject.onNext(
                            SuccessAuthResponse(
                                user = UserDto(
                                    isActivated = response.data?.login?.user?.isActivated ?: false,
                                    personalInfo = ProfileDto(
                                        name = response.data?.login?.user?.personalInfo?.name ?: "",
                                        description = response.data?.login?.user?.personalInfo?.description ?: ""
                                    )
                                ),
                                accessToken = response.data?.login?.accessToken ?: ""
                            )
                        )
                    }
                }

                override fun onFailure(e: ApolloException) {
                    subject.onError(e)
                }
            })
        return subject
    }

    fun register(email: String, pass: String): Observable<Unit> {
        val subject = PublishSubject.create<Unit>()
        val registerMutation = RegisterUserMutation(email, pass)

        NetworkService
            .getInstance()
            ?.getApolloClient()
            ?.mutate(registerMutation)
            ?.enqueue(object : ApolloCall.Callback<RegisterUserMutation.Data>() {
                override fun onResponse(response: Response<RegisterUserMutation.Data>) {
                    if (!response.hasErrors()) {
                        val result = response.data
                        Log.d("TAG", "onResponse: $result")
                        subject.onComplete()
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d("TAG", "onFailure: $e")
                    subject.onError(e)
                }
            })
        return subject
    }
}