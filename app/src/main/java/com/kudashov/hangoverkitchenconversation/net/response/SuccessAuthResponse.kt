package com.kudashov.hangoverkitchenconversation.net.response

import com.kudashov.hangoverkitchenconversation.data.User

data class SuccessAuthResponse(
    val user: User,
    val accessToken: String
)