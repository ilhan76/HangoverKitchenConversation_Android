package com.kudashov.hangoverkitchenconversation.interactor

import com.kudashov.hangoverkitchenconversation.data.Profile
import com.kudashov.hangoverkitchenconversation.net.repository.AuthRepository
import com.kudashov.hangoverkitchenconversation.net.response.SuccessAuthResponse
import com.kudashov.hangoverkitchenconversation.util.io
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class AuthInteractor(
    private val repository: AuthRepository
) {

    fun login(email: String, pass: String): Observable<SuccessAuthResponse> {
        return repository.login(email, pass).io()
    }

    fun register(email: String, pass: String): Completable {
        return repository.register(email, pass).io()
    }

    fun updateProfileInfo(token: String, name: String, description: String) : Observable<Profile> {
        return repository.updateProfileInfo(token, name, description).io()
    }

    fun logout() : Completable {
        return  repository.logout().io()
    }
}