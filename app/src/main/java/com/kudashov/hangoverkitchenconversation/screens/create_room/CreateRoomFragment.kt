package com.kudashov.hangoverkitchenconversation.screens.create_room

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kudashov.hangoverkitchenconversation.interactor.RoomInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.net.repository.RoomRepository
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments
import com.kudashov.hangoverkitchenconversation.util.viewModelsFactory
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_create_room.*
import kotlinx.android.synthetic.main.fragment_create_room.placeholder

class CreateRoomFragment : Fragment(R.layout.fragment_create_room) {

    private val viewModel by viewModelsFactory {
        CreateRoomViewModel(
            roomInteractor = RoomInteractor(RoomRepository()),
            sharedPrefInteractor = SharedPrefInteractor(requireContext())
        )
    }

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
                tie_description.text.toString().isNotEmpty() ||
                etv_limit_input.text.toString().isNotEmpty()
            ) {
                viewModel.createRoom(
                    title = etv_title_input.text.toString(),
                    description = tie_description.text.toString(),
                    isOpen = sc_is_open.isChecked,
                    canSendAnonymousMessage = sc_can_send_anonymous_message.isChecked,
                    limit = etv_limit_input.text.toString().toInt()
                )
            }
            // todo обработать невалидное состояние
        }
    }

    private fun render(state: BaseState) {
        when(state){
            BaseState.Default -> {
                placeholder.visibility = View.GONE
            }
            is BaseState.Error -> {
                placeholder.visibility = View.GONE
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
            BaseState.Loading -> {
                placeholder.visibility = View.VISIBLE
            }
            is BaseState.Success<*> -> {
                placeholder.visibility = View.GONE
                findNavController().navigate(
                    R.id.action_createRoomFragment_to_roomFragment,
                    bundleOf(Arguments.ROOM_ID to state.content)
                )
            }
        }
    }
}