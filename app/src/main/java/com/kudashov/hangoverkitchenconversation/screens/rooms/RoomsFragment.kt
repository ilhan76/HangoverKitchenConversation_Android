package com.kudashov.hangoverkitchenconversation.screens.rooms

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.kudashov.hangoverkitchenconversation.adapters.RoomsAdapter
import com.kudashov.hangoverkitchenconversation.adapters.delegates.RoomItemClickDelegate
import com.kudashov.hangoverkitchenconversation.data.RoomItem
import com.kudashov.hangoverkitchenconversation.interactor.RoomInteractor
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import com.kudashov.hangoverkitchenconversation.net.repository.RoomRepository
import com.kudashov.hangoverkitchenconversation.util.BaseState
import com.kudashov.hangoverkitchenconversation.util.constants.Arguments
import com.kudashov.hangoverkitchenconversation.util.logDebug
import com.kudashov.hangoverkitchenconversation.util.viewModelsFactory
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_rooms.*

class RoomsFragment : Fragment(R.layout.fragment_rooms), RoomItemClickDelegate {

    private val adapter: RoomsAdapter = RoomsAdapter()

    private val viewModel by viewModelsFactory {
        RoomsViewModel(
            roomInteractor = RoomInteractor(RoomRepository()),
            sharedPrefInteractor = SharedPrefInteractor(requireContext())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getAllRooms()
    }

    private fun initView() {
        viewModel.liveData.observe(viewLifecycleOwner, ::render)
        adapter.attachDelegate(this)
        rv_rooms.adapter = adapter
        iv_profile.setOnClickListener {
            findNavController().navigate(R.id.action_roomsFragment_to_profileFragment)
        }
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_roomsFragment_to_createRoomFragment)
        }
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                onTabClicked(tab?.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabClicked(tab?.position)
            }

        })
    }

    private fun render(state: BaseState) {
        when (state) {
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
                logDebug(state.content.toString())
                adapter.setList(state.content as List<RoomItem>)
//                rv_rooms.smoothScrollToPosition(adapter.itemCount)
            }
        }
    }

    private fun onTabClicked(position: Int?) {
        adapter.setList(emptyList())
        when (position) {
            0 -> viewModel.getAllRooms()
            1 -> viewModel.getOwnRooms()
            2 -> viewModel.getManegedRooms()
        }
    }

    override fun toRoomDetail(id: String?) {
        findNavController().navigate(
            R.id.action_roomsFragment_to_roomFragment,
            bundleOf(
                Arguments.ROOM_ID to id
            )
        )
    }
}