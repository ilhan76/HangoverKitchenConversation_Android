package com.kudashov.hangoverkitchenconversation.util

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Set default value for any type of MutableLiveData
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

fun Context.toast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
){
    val toast = Toast.makeText(this, message, duration)
    toast.show()
}

inline fun <reified T : ViewModel> Fragment.viewModelsFactory(crossinline viewModelInitialization: () -> T): Lazy<T> {
    return viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelInitialization.invoke() as T
            }
        }
    }
}