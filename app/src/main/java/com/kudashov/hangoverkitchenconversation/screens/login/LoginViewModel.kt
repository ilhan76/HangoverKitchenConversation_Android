package com.kudashov.hangoverkitchenconversation.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.net.repository.AuthRepository
import com.kudashov.hangoverkitchenconversation.net.response.SuccessAuthResponse
import com.kudashov.hangoverkitchenconversation.util.Arguments
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.IncorrectPassOrEmail
import com.kudashov.hangoverkitchenconversation.util.default
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginViewModel(
    private val authInteractor: AuthInteractor,
    private val prefInteractor: SharedPrefInteractor
) : ViewModel() {

    private val tag: String = this.javaClass.simpleName

    private val _liveData = MutableLiveData<BaseState>().default(BaseState.Default)
    val liveData: LiveData<BaseState> = _liveData

    fun login(email: String, pass: String) {
        _liveData.value = BaseState.Loading

        authInteractor.login(email, pass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess(it) }, { onError(it) })

    }

    private fun onSuccess(response: SuccessAuthResponse) {
        prefInteractor.putString(Arguments.NAME, response.user.personalInfo?.name ?: "")
        prefInteractor.putString(Arguments.DESCRIPTION, response.user.personalInfo?.description ?: "")

        _liveData.value = BaseState.Success(response.accessToken)
    }

    private fun onError(error: Throwable) {
        val message = when (error) {
            is IncorrectPassOrEmail -> "Неправильный логин или пароль"
            else -> "Что-то пошло не так"
        }
        _liveData.value = BaseState.Error(message)
        Log.d(tag, "login: $error")
    }
}