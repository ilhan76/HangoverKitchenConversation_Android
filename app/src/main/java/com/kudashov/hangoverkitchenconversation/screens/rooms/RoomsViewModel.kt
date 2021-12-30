package com.kudashov.hangoverkitchenconversation.screens.rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.data.RoomItem
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.interactor.RoomInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.util.*
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments

sealed class RoomsState {
    object Default : RoomsState()
    object Loading : RoomsState()

    data class Error(val message: String) : RoomsState()
    data class LoadedRooms<T>(val content: T) : RoomsState()

    object Logout : RoomsState()
}

class RoomsViewModel(
    private val roomInteractor: RoomInteractor,
    private val authInteractor: AuthInteractor,
    private val sharedPrefInteractor: SharedPrefInteractor
) : ViewModel() {

    private val _liveData = MutableLiveData<RoomsState>().default(RoomsState.Default)
    val liveData: LiveData<RoomsState> = _liveData

    fun getAllRooms() {
        _liveData.value = RoomsState.Loading

        roomInteractor.getAllRooms(
            token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN),
        ).main().subscribe({ onSuccess(it) }, { onError(it) })
    }

    fun getOwnRooms() {
        _liveData.value = RoomsState.Loading

        roomInteractor.getOwnRooms(
            token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN),
        ).main().subscribe({ onSuccess(it) }, { onError(it) })
    }

    fun logout() {
        _liveData.value = RoomsState.Loading

        authInteractor.logout().main().subscribe({
            _liveData.value = RoomsState.Logout
        }, { onError(it) })
    }

    private fun onSuccess(list: List<RoomItem>) {
        logDebug("onSuccess: Loaded list of room")
        _liveData.value = RoomsState.LoadedRooms(list)
    }

    private fun onError(e: Throwable) {
        logError("onError: $e")
        _liveData.value = RoomsState.Error(
            when (e) {
                is NoItems -> "Не удалось загрузить список комнат"
                else -> "Что-то пошло не так"
            }
        )
    }
}