package com.kudashov.hangoverkitchenconversation.util

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

// Set default value for any type of MutableLiveData
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

fun Context.toast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
){
    val toast = Toast.makeText(this, message, duration)
    toast.show()
}