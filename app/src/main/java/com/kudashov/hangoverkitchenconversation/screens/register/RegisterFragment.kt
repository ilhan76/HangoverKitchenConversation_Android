package com.kudashov.hangoverkitchenconversation.screens.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.net.repository.AuthRepository
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.viewModelsFactory
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel by viewModelsFactory {
        RegisterViewModel(
            authInteractor = AuthInteractor(AuthRepository())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.liveData.observe(viewLifecycleOwner, ::render)

        btn_register.setOnClickListener {
            viewModel.register(
                email = etv_email_input.text.toString(),
                pass = etv_pass_confirm_input.text.toString()
            )
        }
    }

    private fun render(state: BaseState) {
        when (state){
            BaseState.Default -> {}
            is BaseState.Error -> {
                etv_email.error = state.message
                etv_pass.error = state.message
                etv_pass_confirm.error = state.message
            }
            BaseState.Loading -> {}
            is BaseState.Success<*> -> {
                findNavController().navigate(R.id.action_registerFragment_to_fillProfileFragment)
            }
        }
    }
}