package com.kudashov.hangoverkitchenconversation.screens.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kudashov.hangoverkitchenconversation.net.response.SuccessAuthResponse
import com.kudashov.hangoverkitchenconversation.util.Arguments
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.toast
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
/*        etv_pass_input.setOnFocusChangeListener { view, b ->
            Log.d("TAG", "initViews: ")
            if (view.isFocused) {
                etv_pass.error = "In focus"
            } else {
                etv_pass.error = "Out of focus"
            }
        }*/
        btn_login.setOnClickListener {
            viewModel.login(
                email = etv_email_input.text.toString(),
                pass = etv_pass_input.text.toString()
            )
        }
        btn_forgot_pass.setOnClickListener {
            requireContext().toast("дьвмщвьпщь")
        }

        viewModel.liveData.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: BaseState) {
        when(state){
            BaseState.Default -> {
                placeholder.isVisible = false
            }
            is BaseState.Error -> {
                placeholder.isVisible = false
            }
            BaseState.Loading -> {
                placeholder.isVisible = true
            }
            is BaseState.Success<*> -> {
                placeholder.isVisible = false
                findNavController().navigate(
                    R.id.action_loginFragment_to_roomsFragment,
                    bundleOf(
                        Arguments.ACCESS_TOKEN to state.content
                    )
                )
            }
        }
    }
}