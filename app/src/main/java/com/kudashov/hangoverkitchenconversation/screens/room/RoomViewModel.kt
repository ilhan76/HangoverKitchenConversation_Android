package com.kudashov.hangoverkitchenconversation.screens.room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.data.Message
import com.kudashov.hangoverkitchenconversation.data.MessageItem
import com.kudashov.hangoverkitchenconversation.interactor.MessagesInteractor
import com.kudashov.hangoverkitchenconversation.interactor.RoomInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.util.*
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments
import io.reactivex.rxjava3.core.Observable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

sealed class RoomState {
    object Default : RoomState()
    object Loading : RoomState()

    data class DoesNotBelongToTheRoom(val message: String?) : RoomState()
    object BelongToTheRoom : RoomState()

    data class Error(val message: String) : RoomState()
    data class LoadedMessages<T>(val content: T) : RoomState()

    object MessageHasBeenSend : RoomState()
}

class RoomViewModel(
    private val messagesInteractor: MessagesInteractor,
    private val sharedPrefInteractor: SharedPrefInteractor,
    private val roomInteractor: RoomInteractor
) : ViewModel() {

    private val _liveData = MutableLiveData<RoomState>().default(RoomState.Default)
    val liveData: LiveData<RoomState> = _liveData

    fun checkGroupMembership(roomId: String) {
        _liveData.value = RoomState.Loading

        roomInteractor.isUserMemberedInRoom(
            token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN),
            id = roomId
        ).main().subscribe({ isMembered ->
            _liveData.value = if (isMembered) {
                RoomState.BelongToTheRoom
            } else {
                RoomState.DoesNotBelongToTheRoom(null)
            }
        }, { handleError(it) })
    }

    fun joinRoom(roomId: String) {
        _liveData.value = RoomState.Loading

        roomInteractor.joinRoom(
            token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN),
            id = roomId
        ).main().subscribe({
            _liveData.value = RoomState.BelongToTheRoom
        }, { handleError(it) })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMessages(roomId: String) {
        _liveData.value = RoomState.Loading

        messagesInteractor.getMessages(
            token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN),
            roomId = roomId
        ).main().flatMap { list ->
            val username = sharedPrefInteractor.getString(Arguments.NAME)
            Observable.just(list.map { message ->
                MessageItem(
                    text = message.text,
                    name = message.author.name,
                    date = message.date,
                    isMyMessage = message.author.name == username
                )
            })
        }.subscribe({ list ->
            logDebug("onSuccess: Loaded list of room")
            _liveData.value = RoomState.LoadedMessages(
                list.sortedWith(compareBy {
                    LocalDateTime.parse(
                        it.date.substring(0..18),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    )
                }))
        }, { handleError(it) })
    }

    fun sendMessages(
        roomId: String,
        text: String,
        isAnonymous: Boolean
    ) {
        _liveData.value = RoomState.Loading

        messagesInteractor.sendMessages(
            token = sharedPrefInteractor.getString(Arguments.ACCESS_TOKEN),
            roomId = roomId,
            text = text,
            isAnonymous = isAnonymous
        ).main().subscribe({
            _liveData.value = RoomState.MessageHasBeenSend
        }, { handleError(it) })
    }

    private fun handleError(e: Throwable) {
        logError("onError: $e")
        _liveData.value = when (e) {
            is NoItems -> RoomState.Error("Не удалось загрузить список комнат")
            is FailToJoinRoom -> RoomState.DoesNotBelongToTheRoom("Не удалось присоединиться к комнате")
            else -> RoomState.Error("Что-то пошло не так")
        }
    }
}