package com.kudashov.hangoverkitchenconversation.net.repository

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Input
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
import com.kudashov.hangoverkitchenconversation.util.*
import com.kudashov.hangoverkitchenconversation.util.constants.ErrorCodes.BAD_USER_INPUT
import com.kudashov.hangoverkitchenconversation.util.constants.RequestParams.CODE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class AuthRepository {

    //fixme понять, какого хрена не работает Single

    fun login(email: String, pass: String): Observable<SuccessAuthResponse> {
        val hub = PublishSubject.create<SuccessAuthResponse>()
        val loginQuery = LoginQuery(email, pass)

        NetworkService
            .getInstance()
            ?.getApolloClient()
            ?.query(loginQuery)
            ?.enqueue(object : ApolloCall.Callback<LoginQuery.Data>() {
                override fun onResponse(response: Response<LoginQuery.Data>) {
                    logDebug("onResponse: ${response.data}")

                    if (!response.hasErrors()) {
                        val successAuthResponse = response.data?.login?.toSuccessAuthResponse()
                        logDebug(successAuthResponse.toString())
                        hub.onNext(successAuthResponse)
                    } else {
                        Log.d("TAG", "onResponse: ${response.errors}")
                        hub.onError(IncorrectPassOrEmail())
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d("TAG", "onFailure: $e")
                    hub.onError(e)
                }
            })
        return hub
    }

    fun register(email: String, pass: String): Completable {
        val hub = PublishSubject.create<Unit>()
        val registerMutation = RegisterUserMutation(email, pass)

        NetworkService
            .getInstance()
            ?.getApolloClient()
            ?.mutate(registerMutation)
            ?.enqueue(object : ApolloCall.Callback<RegisterUserMutation.Data>() {
                override fun onResponse(response: Response<RegisterUserMutation.Data>) {
                    logDebug("onResponse: ${response.data}")

                    if (!response.hasErrors()) {
                        when (response.data?.register) {
                            RegisterResult.FAILED -> hub.onError(RegisterFailed())
                            RegisterResult.SUCCESS -> hub.onComplete()
                            else -> hub.onError(RegisterFailed())
                        }
                        hub.onComplete()
                    } else {
                        val exception =
                            when (response.errors?.first()?.customAttributes?.get(CODE)) {
                                BAD_USER_INPUT -> EmailAlreadyExist()
                                else -> RegisterFailed()
                            }
                        hub.onError(exception)
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d("TAG", "onFailure: $e")
                    handleError(e, hub)
                }
            })

        return Completable.fromObservable(hub)
    }

    fun updateProfileInfo(token: String, name: String, description: String): Observable<Profile> {
        val hub = PublishSubject.create<Profile>()
        val mutation = UpdateUserMutation(
            UpdateProfileInput(
                Input.fromNullable(name),
                Input.fromNullable(description)
            )
        )

        NetworkService.getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.mutate(mutation)
            ?.enqueue(object : ApolloCall.Callback<UpdateUserMutation.Data>() {
                override fun onResponse(response: Response<UpdateUserMutation.Data>) {
                    logDebug("onResponse: ${response.data}")

                    if (!response.hasErrors()) {
                        hub.onNext(response.data?.updateProfileInfo?.toProfile())
                    } else {
                        hub.onError(FailToUpdateProfileInfo())
                    }
                }

                override fun onFailure(e: ApolloException) {
                    handleError(e, hub)
                }

            })

        return hub
    }

    private fun <T> handleError(e: ApolloException, hub: Subject<T>) {
        logError("onFailure: $e")
        hub.onError(e)
    }
}