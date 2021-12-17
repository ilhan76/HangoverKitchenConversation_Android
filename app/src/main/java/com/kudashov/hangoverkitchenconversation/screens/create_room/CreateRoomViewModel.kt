package com.kudashov.hangoverkitchenconversation.screens.create_room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.default

class CreateRoomViewModel(

): ViewModel() {

    private val tag: String = this.javaClass.simpleName

    private val _liveData = MutableLiveData<BaseState>().default(BaseState.Default)
    val liveData: LiveData<BaseState> = _liveData

    fun createRoom(
        title: String,
        description: String,
        isClosed: Boolean,
        canSendAnonimusMessage: Boolean,
        limit: Int
    ){
        _liveData.value = BaseState.Loading


    }
}