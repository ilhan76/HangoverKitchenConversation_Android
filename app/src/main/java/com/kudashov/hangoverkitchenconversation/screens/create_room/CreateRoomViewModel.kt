package com.kudashov.hangoverkitchenconversation.screens.create_room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.interactor.RoomInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.FailToCreateRoom
import com.kudashov.hangoverkitchenconversation.util.IncorrectPassOrEmail
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments
import com.kudashov.hangoverkitchenconversation.util.default
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class CreateRoomViewModel(
    val roomInteractor: RoomInteractor,
    val sharedPrefInteractor: SharedPrefInteractor
) : ViewModel() {

    private val tag: String = this.javaClass.simpleName

    private val _liveData = MutableLiveData<BaseState>().default(BaseState.Default)
    val liveData: LiveData<BaseState> = _liveData

    fun createRoom(
        title: String,
        description: String,
        isOpen: Boolean,
        canSendAnonymousMessage: Boolean,
        limit: Int
    ) {
        _liveData.value = BaseState.Loading
        val token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN)

        roomInteractor.createRoom(
            token = token,
            title = title,
            description = description,
            isOpen = isOpen,
            canSendAnonymousMessage = canSendAnonymousMessage,
            limit = limit
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _liveData.value = BaseState.Success(it)
            }, {
                onError(it)
            })
    }

    private fun onError(error: Throwable) {
        val message = when (error) {
            is FailToCreateRoom -> "Не удалось создать комнату"
            else -> "Что-то пошло не так"
        }
        _liveData.value = BaseState.Error(message)
        Log.d(tag, "login: $error")
    }
}