package com.phenikaa.h1_robot_app.presentation.features.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.domain.model.Position
import com.phenikaa.h1_robot_app.domain.model.NavigationState
import com.phenikaa.h1_robot_app.domain.usecase.navigation.GetCurrentPositionUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToPositionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val getCurrentPositionUseCase: GetCurrentPositionUseCase,
    private val navigateToPositionUseCase: NavigateToPositionUseCase
) : ViewModel() {

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.Idle)
    val navigationState: StateFlow<NavigationState> = _navigationState

    private val _currentPosition = MutableStateFlow<Position?>(null)
    val currentPosition: StateFlow<Position?> = _currentPosition

    fun getCurrentPosition() {
        Log.d("NavigationViewModel", "getCurrentPosition called")
        viewModelScope.launch {
            try {
                val position = getCurrentPositionUseCase()
                _currentPosition.value = position

                Log.d("NavigationViewModel", "Current position: $position")
            } catch (e: Exception) {
                Log.d("NavigationViewModel", "Error getting current position: $e")
            }
        }
    }

    fun navigateToPosition(position: Position) {
        viewModelScope.launch {
            navigateToPositionUseCase(position).collect { state ->
                _navigationState.value = state
            }
        }
    }
}
