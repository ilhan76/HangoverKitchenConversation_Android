package com.kudashov.hangoverkitchenconversation.screens.room

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_room.*

class RoomFragment : Fragment(R.layout.fragment_room) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        iv_back.setOnClickListener {
            findNavController().navigate(R.id.action_roomFragment_to_roomsFragment)
        }
    }
}