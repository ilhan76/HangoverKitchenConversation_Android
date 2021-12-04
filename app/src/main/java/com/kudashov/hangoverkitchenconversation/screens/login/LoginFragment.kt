package com.kudashov.hangoverkitchenconversation.screens.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        etv_pass_input.setOnFocusChangeListener { view, b ->
            Log.d("TAG", "initViews: ")
            if (view.isFocused) {
                etv_pass.error = "In focus"
            } else {
                etv_pass.error = "Out of focus"
            }
        }
    }
}