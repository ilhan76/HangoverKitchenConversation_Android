package com.kudashov.hangoverkitchenconversation.net.repository

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.kudashov.hangoverkitchenconversation.LoginQuery
import com.kudashov.hangoverkitchenconversation.RegisterUserMutation
import com.kudashov.hangoverkitchenconversation.UpdateUserMutation
import com.kudashov.hangoverkitchenconversation.data.domain.Profile
import com.kudashov.hangoverkitchenconversation.data.dto.ProfileDto
import com.kudashov.hangoverkitchenconversation.data.dto.UserDto
import com.kudashov.hangoverkitchenconversation.net.NetworkService
import com.kudashov.hangoverkitchenconversation.net.response.SuccessAuthResponse
import com.kudashov.hangoverkitchenconversation.type.RegisterResult
import com.kudashov.hangoverkitchenconversation.type.UpdateProfileInput
import com.kudashov.hangoverkitchenconversation.util.EmailAlreadyExist
import com.kudashov.hangoverkitchenconversation.util.constants.ErrorCodes.BAD_USER_INPUT
import com.kudashov.hangoverkitchenconversation.util.IncorrectPassOrEmail
import com.kudashov.hangoverkitchenconversation.util.RegisterFailed
import com.kudashov.hangoverkitchenconversation.util.constants.RequestParams.CODE
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

    fun register(email: String, pass: String): Observable<Unit> {
        val subject = PublishSubject.create<Unit>()
        val registerMutation = RegisterUserMutation(email, pass)

        NetworkService
            .getInstance()
            ?.getApolloClient()
            ?.mutate(registerMutation)
            ?.enqueue(object : ApolloCall.Callback<RegisterUserMutation.Data>() {
                override fun onResponse(response: Response<RegisterUserMutation.Data>) {
                    val result = response.data
                    Log.d("TAG", "onResponse: $result")

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
                    subject.onError(e)
                }
            })

        return subject
    }

    fun updateProfileInfo(token: String, profile: UpdateProfileInput): Observable<Profile> {
        val subject = PublishSubject.create<Profile>()
        val mutation = UpdateUserMutation(profile)

        NetworkService.getInstance()
            ?.getApolloClientWithTokenInterceptor(token)
            ?.mutate(mutation)
            ?.enqueue(object : ApolloCall.Callback<UpdateUserMutation.Data>() {
                override fun onResponse(response: Response<UpdateUserMutation.Data>) {
                    if (!response.hasErrors()) {
                        subject.onNext(response.data?.updateProfileInfo?.toProfile())
                    }
                }

                override fun onFailure(e: ApolloException) {
                    subject.onError(e)
                }

            })

        return subject
    }

    private fun LoginQuery.Login.toSuccessAuthResponse() = SuccessAuthResponse(
        user = UserDto(
            isActivated = this.user.isActivated,
            personalInfo = ProfileDto(
                name = this.user.personalInfo?.name ?: "",
                description = this.user.personalInfo?.description
                    ?: ""
            )
        ),
        accessToken = this.accessToken
    )

    private fun UpdateUserMutation.UpdateProfileInfo.toProfile() = Profile(
        name = this.name ?: "",
        description = this.description ?: ""
    )
}