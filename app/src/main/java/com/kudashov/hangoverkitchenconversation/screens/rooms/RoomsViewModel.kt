package com.kudashov.hangoverkitchenconversation.screens.rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.data.RoomItem
import com.kudashov.hangoverkitchenconversation.interactor.RoomInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.util.*
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments

class RoomsViewModel(
    private val roomInteractor: RoomInteractor,
    private val sharedPrefInteractor: SharedPrefInteractor
) : ViewModel() {

    private val _liveData = MutableLiveData<BaseState>().default(BaseState.Default)
    val liveData: LiveData<BaseState> = _liveData

    fun getAllRooms() {
        _liveData.value = BaseState.Loading

        roomInteractor.getAllRooms(
            token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN),
        ).main().subscribe({ onSuccess(it) }, { onError(it) })
    }

    fun getOwnRooms() {
        _liveData.value = BaseState.Loading

        roomInteractor.getOwnRooms(
            token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN),
        ).main().subscribe({ onSuccess(it) }, { onError(it) })
    }

    private fun onSuccess(list: List<RoomItem>) {
        logDebug("onSuccess: Loaded list of room")
        _liveData.value = BaseState.Success(list)
    }

    private fun onError(e: Throwable) {
        logError("onError: $e")
        _liveData.value = BaseState.Error(
            when (e) {
                is NoItems -> "Не удалось загрузить список комнат"
                else -> "Что-то пошло не так"
            }
        )
    }
}