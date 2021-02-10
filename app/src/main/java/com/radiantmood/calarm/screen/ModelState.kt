package com.radiantmood.calarm.screen

sealed class ModelState
object LoadingState : ModelState()
object FinishedState : ModelState()
class ErrorState(val err: Exception) : ModelState()