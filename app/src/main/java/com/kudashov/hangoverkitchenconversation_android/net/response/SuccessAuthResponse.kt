package com.kudashov.hangoverkitchenconversation_android.net.response

import com.kudashov.hangoverkitchenconversation_android.data.dto.UserDto

data class SuccessAuthResponse(
    val user: UserDto,
    val accessToken: String
)