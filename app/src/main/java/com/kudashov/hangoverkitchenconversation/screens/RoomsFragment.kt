package com.kudashov.hangoverkitchenconversation.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.kudashov.hangoverkitchenconversation_android.R
import kotlinx.android.synthetic.main.fragment_rooms.*

class RoomsFragment : Fragment(R.layout.fragment_rooms) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        iv_profile.setOnClickListener {
            findNavController().navigate(R.id.action_roomsFragment_to_profileFragment)
        }
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_roomsFragment_to_createRoomFragment)
        }
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        Toast.makeText(requireContext(), "All Rooms", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        Toast.makeText(requireContext(), "My Rooms", Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(requireContext(), "I am Admin", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }
}