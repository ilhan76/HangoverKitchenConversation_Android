package com.kudashov.hangoverkitchenconversation.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.RegisterFailed
import com.kudashov.hangoverkitchenconversation.util.default
import com.kudashov.hangoverkitchenconversation.util.main
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class RegisterViewModel(
    private val authInteractor: AuthInteractor
) : ViewModel() {

    private val _liveData = MutableLiveData<BaseState>().default(BaseState.Default)
    val liveData: LiveData<BaseState> = _liveData

    fun register(email: String, pass: String) {
        _liveData.value = BaseState.Loading

        authInteractor.register(email, pass)
            .main()
            .subscribe({onComplete()}, {onError(it)})
    }

    private fun onError(error: Throwable){
        val message = when (error) {
            is RegisterFailed -> "Ошибка регистрации"
            else -> "Что-то пошло не так"
        }
        _liveData.value = BaseState.Error(message)
    }

    private fun onComplete(){
        _liveData.value = BaseState.Success(Unit)
    }
}