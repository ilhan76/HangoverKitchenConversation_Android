package com.kudashov.hangoverkitchenconversation.net.repository

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.kudashov.hangoverkitchenconversation.LoginQuery
import com.kudashov.hangoverkitchenconversation.RegisterUserMutation
import com.kudashov.hangoverkitchenconversation.UpdateUserMutation
import com.kudashov.hangoverkitchenconversation.data.Profile
import com.kudashov.hangoverkitchenconversation.net.NetworkService
import com.kudashov.hangoverkitchenconversation.net.response.SuccessAuthResponse
import com.kudashov.hangoverkitchenconversation.type.RegisterResult
import com.kudashov.hangoverkitchenconversation.type.UpdateProfileInput
import com.kudashov.hangoverkitchenconversation.util.EmailAlreadyExist
import com.kudashov.hangoverkitchenconversation.util.constants.ErrorCodes.BAD_USER_INPUT
import com.kudashov.hangoverkitchenconversation.util.IncorrectPassOrEmail
import com.kudashov.hangoverkitchenconversation.util.RegisterFailed
import com.kudashov.hangoverkitchenconversation.util.constants.RequestParams.CODE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class AuthRepository {

    private val tag: String = this.javaClass.simpleName

    fun login(email: String, pass: String): Observable<SuccessAuthResponse> {
        val subject = PublishSubject.create<SuccessAuthResponse>()
        val loginQuery = LoginQuery(email, pass)

        NetworkService
            .getInstance()
            ?.getApolloClient()
            ?.query(loginQuery)
            ?.enqueue(object : ApolloCall.Callback<LoginQuery.Data>() {
                override fun onResponse(response: Response<LoginQuery.Data>) {
                    Log.d(tag, "onResponse: ${response.data}")

                    if (!response.hasErrors()) {
                        response.data
                        subject.onNext(response.data?.login?.toSuccessAuthResponse())
                    } else {
                        Log.d("TAG", "onResponse: ${response.errors}")
                        subject.onError(IncorrectPassOrEmail())
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d("TAG", "onFailure: $e")
                    subject.onError(e)
                }
            })
        return subject
    }

    fun register(email: String, pass: String): Completable {
        val subject = PublishSubject.create<Unit>()
        val registerMutation = RegisterUserMutation(email, pass)

        NetworkService
            .getInstance()
            ?.getApolloClient()
            ?.mutate(registerMutation)
            ?.enqueue(object : ApolloCall.Callback<RegisterUserMutation.Data>() {
                override fun onResponse(response: Response<RegisterUserMutation.Data>) {
                    Log.d(tag, "onResponse: ${response.data}")

                    if (!response.hasErrors()) {
                        when (response.data?.register) {
                            RegisterResult.FAILED -> subject.onError(RegisterFailed())
                            RegisterResult.SUCCESS -> subject.onComplete()
                            else -> subject.onError(RegisterFailed())
                        }
                        subject.onComplete()
                    } else {
                        val exception =
                            when (response.errors?.first()?.customAttributes?.get(CODE)) {
                                BAD_USER_INPUT -> EmailAlreadyExist()
                                else -> RegisterFailed()
                            }
                        subject.onError(exception)
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d("TAG", "onFailure: $e")
                    handleError(e, subject)
                }
            })

        return Completable.fromObservable(subject)
    }

    fun updateProfileInfo(token: String, profile: UpdateProfileInput): Observable<Profile> {
        val subject = PublishSubject.create<Profile>()
        val mutation = UpdateUserMutation(profile)

        NetworkService.getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.mutate(mutation)
            ?.enqueue(object : ApolloCall.Callback<UpdateUserMutation.Data>() {
                override fun onResponse(response: Response<UpdateUserMutation.Data>) {
                    Log.d(tag, "onResponse: ${response.data}")

                    if (!response.hasErrors()) {
                        subject.onNext(response.data?.updateProfileInfo?.toProfile())
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