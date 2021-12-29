package com.kudashov.hangoverkitchenconversation.screens.room

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kudashov.hangoverkitchenconversation.adapters.MessagesAdapter
import com.kudashov.hangoverkitchenconversation.data.MessageItem
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
import kotlinx.android.synthetic.main.layout_room_placeholder.*

class RoomFragment : Fragment(R.layout.fragment_room) {

    private val adapter: MessagesAdapter = MessagesAdapter()

    private val viewModel by viewModelsFactory {
        RoomViewModel(
            messagesInteractor = MessagesInteractor(MessagesRepository()),
            sharedPrefInteractor = SharedPrefInteractor(requireContext()),
            roomInteractor = RoomInteractor(RoomRepository())
        )
    }

    private lateinit var roomId : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomId = arguments?.getString(Arguments.ROOM_ID)!!
        initView()
    }

    private fun initView() {
        viewModel.liveData.observe(viewLifecycleOwner, ::render)
        rv_messages.adapter = adapter
        //viewModel.observeNewMessage(roomId)
        iv_send.setOnClickListener {
            if (etv_message.text.isNotEmpty()) {
                viewModel.sendMessages(
                    roomId = roomId,
                    text = etv_message.text.toString(),
                    isAnonymous = false
                )
            }
        }
        btn_join_room.setOnClickListener {
            viewModel.joinRoom(roomId)
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
                logDebug("_________________")
                logDebug(state.content.toString())
                logDebug("_________________")
                adapter.setList(state.content as List<MessageItem>)
                rv_messages.smoothScrollToPosition(adapter.itemCount)
            }
            RoomState.MessageHasBeenSend -> {
                placeholder.visibility = View.GONE
                etv_message.setText("")
                //viewModel.getMessages(roomId)
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
        placeholder_background.isVisible = false
        progress_bar.isVisible = false
        layout_join_room.isVisible = true
    }
}