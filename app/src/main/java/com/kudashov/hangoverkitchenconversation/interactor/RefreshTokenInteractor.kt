package com.kudashov.hangoverkitchenconversation.interactor

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class RefreshTokenInteractor {

    fun refresh(): Observable<String>{
        val subject = PublishSubject.create<String>()

        return subject
    }
}