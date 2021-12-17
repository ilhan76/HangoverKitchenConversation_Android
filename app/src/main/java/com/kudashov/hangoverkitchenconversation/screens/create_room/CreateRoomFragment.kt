package com.kudashov.hangoverkitchenconversation.screens.create_room

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.viewModelsFactory
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_create_room.*

class CreateRoomFragment : Fragment(R.layout.fragment_create_room) {

    private val viewModel by viewModelsFactory { CreateRoomViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.liveData.observe(viewLifecycleOwner, ::render)
        iv_back.setOnClickListener {
            findNavController().navigate(R.id.action_createRoomFragment_to_roomsFragment)
        }
        btn_create.setOnClickListener {
            if (
                etv_title_input.text.toString().isNotEmpty() ||
                etv_description_input.text.toString().isNotEmpty() ||
                etv_limit_input.text.toString().isNotEmpty()
            ) {
                viewModel.createRoom(
                    title = etv_title_input.text.toString(),
                    description = etv_description_input.text.toString(),
                    isClosed = sc_is_open.isChecked,
                    canSendAnonimusMessage = sc_can_send_anonymous_message.isChecked,
                    limit = etv_limit_input.text.toString().toInt()
                )
            }
            // todo обработать невалидное состояние
        }
    }

    private fun render(state: BaseState) {
        when(state){
            BaseState.Default -> {

            }
            is BaseState.Error -> {

            }
            BaseState.Loading -> {
                placeholder.visibility = View.VISIBLE
            }
            is BaseState.Success<*> -> {
                // todo переход на экран комнаты
            }
        }
    }
}