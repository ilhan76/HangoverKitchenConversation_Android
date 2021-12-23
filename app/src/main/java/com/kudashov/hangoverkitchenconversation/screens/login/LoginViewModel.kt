package com.kudashov.hangoverkitchenconversation.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.util.*
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments

class LoginViewModel(
    private val authInteractor: AuthInteractor,
    private val prefInteractor: SharedPrefInteractor
) : ViewModel() {

    private val _liveData = MutableLiveData<BaseState>().default(BaseState.Default)
    val liveData: LiveData<BaseState> = _liveData

    fun login(email: String, pass: String) {
        _liveData.value = BaseState.Loading

        authInteractor.login(
            email = email,
            pass = pass
        ).main().subscribe({ response ->
            prefInteractor.putString(Arguments.NAME, response.user.personalInfo?.name ?: "")
            prefInteractor.putString(
                Arguments.DESCRIPTION,
                response.user.personalInfo?.description ?: ""
            )
            prefInteractor.putString(Arguments.ACCESS_TOKEN, response.accessToken)
            //fixme - Обработать нектевированный профиль

            _liveData.value = BaseState.Success(response)
        }, {
            handleError(it)
        })
    }

    private fun handleError(error: Throwable) {
        logError("login: $error")
        _liveData.value = when (error) {
            is IncorrectPassOrEmail -> BaseState.Error("Неправильный логин или пароль")
            else -> BaseState.Error("Что-то пошло не так")
        }
    }
}