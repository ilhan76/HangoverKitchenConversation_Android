package com.kudashov.hangoverkitchenconversation.screens.room

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kudashov.hangoverkitchenconversation.interactor.MessagesInteractor
import com.kudashov.hangoverkitchenconversation.interactor.RoomInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.net.repository.MessagesRepository
import com.kudashov.hangoverkitchenconversation.net.repository.RoomRepository
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments
import com.kudashov.hangoverkitchenconversation.util.logDebug
import com.kudashov.hangoverkitchenconversation.util.viewModelsFactory
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_room.*
import kotlinx.android.synthetic.main.layout_placeholder.*
import kotlinx.android.synthetic.main.layout_placeholder.view.*

class RoomFragment : Fragment(R.layout.fragment_room) {

    private val viewModel by viewModelsFactory {
        RoomViewModel(
            messagesInteractor = MessagesInteractor(MessagesRepository()),
            sharedPrefInteractor = SharedPrefInteractor(requireContext()),
            roomInteractor = RoomInteractor(RoomRepository())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.liveData.observe(viewLifecycleOwner, ::render)
        iv_send.setOnClickListener {
            if (etv_message.text.isNotEmpty()) {
                viewModel.sendMessages(
                    roomId = arguments?.getString(Arguments.ROOM_ID)!!,
                    text = etv_message.text.toString(),
                    isAnonymous = false,
                    photos = null
                )
            }
        }
        btn_join_room.setOnClickListener {
            viewModel.joinRoom(arguments?.getString(Arguments.ROOM_ID)!!)
        }
        iv_back.setOnClickListener {
            findNavController().navigate(R.id.action_roomFragment_to_roomsFragment)
        }
    }

    private fun render(state: RoomState) {
        when (state) {
            RoomState.Default -> {
                placeholder.visibility = View.GONE
                viewModel.checkGroupMembership(arguments?.getString(Arguments.ROOM_ID)!!)
            }
            is RoomState.Error -> {
                placeholder.visibility = View.GONE
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }
            RoomState.BelongToTheRoom -> {
                viewModel.getMessages(arguments?.getString(Arguments.ROOM_ID)!!)
            }
            is RoomState.DoesNotBelongToTheRoom -> {
                if (state.message != null){
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                showJoinRoomButton()
            }
            RoomState.Loading -> showProgressBar()
            is RoomState.LoadedMessages<*> -> {
                placeholder.visibility = View.GONE
                logDebug(state.content.toString())
/*                adapter.setList(state.content as List<RoomItem>)
                rv_messages.smoothScrollToPosition(adapter.itemCount)*/
            }
        }
    }

    private fun showProgressBar() {
        placeholder.visibility = View.VISIBLE
        with(placeholder) {
            placeholder_background.isVisible = true
            progress_bar.isVisible = true
            layout_join_room.isVisible = false
        }
    }

    private fun showJoinRoomButton() {
        placeholder.visibility = View.VISIBLE
        with(placeholder) {
            placeholder_background.isVisible = false
            progress_bar.isVisible = false
            layout_join_room.isVisible = true
        }
    }
}