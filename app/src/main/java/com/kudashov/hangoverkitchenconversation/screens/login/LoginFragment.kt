package com.kudashov.hangoverkitchenconversation.screens.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.kudashov.hangoverkitchenconversation.data.isFilled
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.net.repository.AuthRepository
import com.kudashov.hangoverkitchenconversation.net.response.SuccessAuthResponse
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.viewModelsFactory
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModelsFactory{
        LoginViewModel(
            authInteractor = AuthInteractor(AuthRepository()),
            prefInteractor = SharedPrefInteractor(requireContext())
        )
    }

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

        btn_register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
                val response = state.content as SuccessAuthResponse

                if (response.user.isFilled()) {
                    findNavController().navigate(
                        R.id.action_loginFragment_to_roomsFragment,
                        bundleOf(
                            Arguments.ACCESS_TOKEN to response.accessToken
                        )
                    )
                } else {
                    findNavController().navigate(
                        R.id.action_loginFragment_to_fillProfileFragment
                    )
                }
            }
        }
    }
}