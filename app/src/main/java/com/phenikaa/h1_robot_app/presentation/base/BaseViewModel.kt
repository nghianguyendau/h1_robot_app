package com.phenikaa.h1_robot_app.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Ali Asadi on 13/05/2020
 */
open class BaseViewModel : ViewModel() {
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job = viewModelScope.launch(block = block)
}
