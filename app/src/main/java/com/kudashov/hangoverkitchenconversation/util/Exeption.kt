package com.kudashov.hangoverkitchenconversation.util

class CustomException(override val message: String?) : Throwable()

class IncorrectPassOrEmail : Throwable()
class LogoutFailed: Throwable()
class RegisterFailed : Throwable()
class EmailAlreadyExist : Throwable()
class FailToCheckIsUserMemberedInRoom : Throwable()

class FailToCreateRoom : Throwable()
class FailToJoinRoom : Throwable()
class FailToLeaveRoom : Throwable()
class NoItems : Throwable()

class FailToUpdateProfileInfo : Throwable()