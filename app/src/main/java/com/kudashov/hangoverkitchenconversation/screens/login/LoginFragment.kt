package com.kudashov.hangoverkitchenconversation.screens.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
                placeholder.visibility = View.GONE
            }
            is BaseState.Error -> {
                placeholder.visibility = View.GONE
                etv_email.error = state.message
                etv_pass.error = state.message
            }
            BaseState.Loading -> {
                placeholder.visibility = View.VISIBLE
            }
            is BaseState.Success<*> -> {
                placeholder.visibility = View.GONE
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