package com.kudashov.hangoverkitchenconversation.util

sealed class BaseState {

    object Default : BaseState()
    object Loading : BaseState()
    data class Error(val message: String) : BaseState()
    data class Success<T>(val content: T) : BaseState()

}
