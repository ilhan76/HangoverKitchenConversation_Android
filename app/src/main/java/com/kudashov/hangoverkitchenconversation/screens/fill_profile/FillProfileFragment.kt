package com.kudashov.hangoverkitchenconversation.screens.fill_profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kudashov.hangoverkitchenconversation.interactor.AuthInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.net.repository.AuthRepository
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.logDebug
import com.kudashov.hangoverkitchenconversation.util.viewModelsFactory
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_register_fill_profile.*

class FillProfileFragment : Fragment(R.layout.fragment_register_fill_profile) {

    private val viewModel by viewModelsFactory {
        FillProfileViewModel(
            authInteractor = AuthInteractor(AuthRepository()),
            sharedPrefInteractor = SharedPrefInteractor(requireContext())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.liveData.observe(viewLifecycleOwner, ::render)

        btn_continue.setOnClickListener {
            viewModel.fillProfileInfo(
                name = tie_name.text.toString(),
                description = tie_description.text.toString()
            )
        }
    }

    private fun render(state: BaseState) {
        when (state){
            BaseState.Default -> {
                placeholder.isVisible = false
            }
            is BaseState.Error -> {
                placeholder.isVisible = false
                logDebug("Error")
            }
            BaseState.Loading -> {
                logDebug("Loading")
                placeholder.isVisible = true
                //todo - добавить лоадер
            }
            is BaseState.Success<*> -> {
                placeholder.isVisible = false
                findNavController().navigate(
                    R.id.action_fillProfileFragment_to_roomsFragment
                )
            }
        }
    }
}