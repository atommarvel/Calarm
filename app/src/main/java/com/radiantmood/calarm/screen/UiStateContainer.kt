package com.radiantmood.calarm.screen

sealed class UiStateContainer<T>(val key: String)

class LoadingUiStateContainer<T> : UiStateContainer<T>("Loading")
class ErrorUiStateContainer<T>(val errorMessage: String? = null) : UiStateContainer<T>("Error")
open class FinishedUiStateContainer<T> : UiStateContainer<T>("Finished")