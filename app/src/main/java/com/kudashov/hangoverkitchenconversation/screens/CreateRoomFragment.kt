package com.kudashov.hangoverkitchenconversation.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_create_room.*

class CreateRoomFragment : Fragment(R.layout.fragment_create_room) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        iv_back.setOnClickListener {
            findNavController().navigate(R.id.action_createRoomFragment_to_roomsFragment)
        }
    }
}