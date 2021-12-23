package com.kudashov.hangoverkitchenconversation.data

data class User(
    val isActivated: Boolean,
    val personalInfo: Profile?
)

fun User.isFilled() : Boolean = personalInfo?.name?.isNotEmpty() == true && personalInfo.description.isNotEmpty()