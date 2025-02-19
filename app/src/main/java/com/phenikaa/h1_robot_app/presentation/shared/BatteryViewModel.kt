package com.phenikaa.h1_robot_app.presentation.shared

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class BatteryViewModel @Inject constructor(
    application: Application
) : ViewModel() {
    private val myApp = application as MyApplication
    val batteryLevel: StateFlow<Int> = myApp.batteryLevel
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, 100)
}