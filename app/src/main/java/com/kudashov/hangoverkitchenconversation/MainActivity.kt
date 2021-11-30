package com.kudashov.hangoverkitchenconversation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.net.NetworkService
import com.kudashov.hangoverkitchenconversation.net.repository.AuthRepository
import com.kudashov.hangoverkitchenconversation_android.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*        AuthInteractor(AuthRepository())
            .login("kudashov.ilya@inbox.ru", "pass123")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("TAG", "onCreate: $it")
            }*/
    }
}