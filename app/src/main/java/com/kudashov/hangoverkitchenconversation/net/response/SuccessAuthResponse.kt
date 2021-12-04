package com.kudashov.hangoverkitchenconversation.net.response

import com.kudashov.hangoverkitchenconversation.data.dto.UserDto

data class SuccessAuthResponse(
    val user: UserDto,
    val accessToken: String
)