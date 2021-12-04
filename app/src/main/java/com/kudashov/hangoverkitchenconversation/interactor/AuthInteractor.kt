package com.kudashov.hangoverkitchenconversation.interactor

import com.kudashov.hangoverkitchenconversation.net.repository.AuthRepository
import com.kudashov.hangoverkitchenconversation.net.response.SuccessAuthResponse
import io.reactivex.rxjava3.core.Observable

class AuthInteractor(
    private val repository: AuthRepository
) {

    fun login(email: String, pass: String): Observable<SuccessAuthResponse> {
        return repository.login(email, pass)
    }

    fun register(email: String, pass: String): Observable<Unit> {
        return repository.register(email, pass)
    }
}