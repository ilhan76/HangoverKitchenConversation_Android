package com.kudashov.hangoverkitchenconversation.screens.fill_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.data.Profile
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.screens.room.RoomState
import com.kudashov.hangoverkitchenconversation.util.*
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable

class FillProfileViewModel(
    private val authInteractor: AuthInteractor,
    private val sharedPrefInteractor: SharedPrefInteractor
) : ViewModel() {

    private val _liveData = MutableLiveData<BaseState>().default(BaseState.Default)
    val liveData: LiveData<BaseState> = _liveData

    fun fillProfileInfo(name: String, description: String) {
        _liveData.value = BaseState.Loading

        authInteractor.updateProfileInfo(
            token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN),
            name = name,
            description = description
        ).main().subscribe({
            _liveData.value = BaseState.Success(it)
        }, {
            handleError(it)
        })
    }

    private fun handleError(e: Throwable) {
        logError("onError: $e")
        _liveData.value = when (e) {
            is NoItems -> BaseState.Error("Не удалось загрузить список комнат")
            else -> BaseState.Error("Что-то пошло не так")
        }
    }
}