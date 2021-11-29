package com.kudashov.hangoverkitchenconversation.data.dto

data class UserDto(
    val isActivated: Boolean,
    val personalInfo: ProfileDto?
)