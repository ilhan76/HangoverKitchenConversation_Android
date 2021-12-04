package com.kudashov.hangoverkitchenconversation.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.net.repository.AuthRepository
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.IncorrectPassOrEmail
import com.kudashov.hangoverkitchenconversation.util.default
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginViewModel(
    private val authInteractor: AuthInteractor = AuthInteractor(AuthRepository())
) : ViewModel() {

    private val tag: String = this.javaClass.simpleName

    private val _liveData = MutableLiveData<BaseState>().default(BaseState.Default)
    val liveData: LiveData<BaseState> = _liveData

    fun login(email: String, pass: String) {
        _liveData.value = BaseState.Loading

        authInteractor.login(email, pass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _liveData.value = BaseState.Success(it.accessToken)
                }, {
                    val message = when (it){
                        is IncorrectPassOrEmail -> "Неправильный логин или пароль"
                        else -> "Что-то пошло не так"
                    }
                    _liveData.value = BaseState.Error(message)
                    Log.d(tag, "login: $it")
                })

    }
}