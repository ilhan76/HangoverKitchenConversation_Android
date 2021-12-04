package com.kudashov.hangoverkitchenconversation.interactor

import android.content.Context
import android.content.SharedPreferences
import com.kudashov.hangoverkitchenconversation.util.Arguments.APP_PREFERENCE

class SharedPrefInteractor(val context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(
        APP_PREFERENCE,
        Context.MODE_PRIVATE
    )

    fun putString(key: String, data: String) {
        val editor = pref.edit()
        editor.putString(key, data)
        editor.apply()
    }
}