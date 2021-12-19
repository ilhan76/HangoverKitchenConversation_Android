package com.kudashov.hangoverkitchenconversation.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

// Set default value for any type of MutableLiveData
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

fun Context.toast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    val toast = Toast.makeText(this, message, duration)
    toast.show()
}

fun Any.logDebug(m: String) {
    Log.d(this::class.java.simpleName, m)
}

fun Any.logError(m: String) {
    Log.e(this::class.java.simpleName, m)
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

fun <T> Observable<T>.io(): Observable<T> = subscribeOn(Schedulers.io())
fun <T> Observable<T>.main(): Observable<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.io(): Single<T> = subscribeOn(Schedulers.io())
fun <T> Single<T>.main(): Single<T> = observeOn(AndroidSchedulers.mainThread())

fun Completable.io(): Completable = subscribeOn(Schedulers.io())
fun Completable.main(): Completable = observeOn(AndroidSchedulers.mainThread())