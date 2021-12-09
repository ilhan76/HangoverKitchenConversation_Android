package com.kudashov.hangoverkitchenconversation.screens.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.net.repository.AuthRepository
import com.kudashov.hangoverkitchenconversation.screens.login.LoginViewModel
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments
import com.kudashov.hangoverkitchenconversation.util.viewModelsFactory
import com.kudashov.hangoverkitchenconversation_android.R

class FillProfileFragment : Fragment(R.layout.fragment_register_fill_profile) {

    private var registerViewModel: RegisterViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        registerViewModel = arguments?.get(Arguments.VIEW_MODEL) as RegisterViewModel
        registerViewModel?.liveData?.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: BaseState) {
        when (state){
            BaseState.Default -> {

            }
            is BaseState.Error -> {}
            BaseState.Loading -> {}
            is BaseState.Success<*> -> {}
        }
    }
}