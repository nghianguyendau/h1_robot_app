package com.phenikaa.h1_robot_app.presentation.ui.password

import com.phenikaa.h1_robot_app.domain.entity.enum.PinPasswordStatus
import com.phenikaa.h1_robot_app.domain.usecase.GetPassWordUseCase
import com.phenikaa.h1_robot_app.domain.usecase.InitPassWordAppUseCase
import javax.inject.Inject
import com.phenikaa.h1_robot_app.presentation.base.BaseViewModel
import com.phenikaa.h1_robot_app.utils.singleSharedFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class PassWordViewModel @Inject constructor(
    private val getPassWordUseCase: GetPassWordUseCase,
    private val initPassWordApp: InitPassWordAppUseCase
) : BaseViewModel() {
    private val _uiState: MutableStateFlow<PassWordState> = MutableStateFlow(PassWordState())
    val state = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<PassWordNavigationState> = singleSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    init {
        onInitPassWordPage()
    }

    private fun onInitPassWordPage() = launch {
        _uiState.update {
            initPassWordApp.invoke()
            val password = getPassWordUseCase.invoke()
            it.copy(passWord = password)
        }
    }

    fun onTapRemove(lPassWord: List<String>) = launch {
        _uiState.update {
            val updatedList = lPassWord.dropLast(1)
            onConfirm(updatedList)
            it.copy(lPassWord = updatedList)
        }
    }

    private fun onConfirm(lPassWord: List<String>) = launch {
        _uiState.update {
            var pinStatus = PinPasswordStatus.ENABLE;
            if (checkPassWord(lPassWord,it.passWord) && lPassWord.size == it.lengthPassWord) {
                _navigationState.tryEmit(PassWordNavigationState.NavigationBar)
                pinStatus = PinPasswordStatus.TRUE
            } else if(lPassWord.size < it.lengthPassWord) {
                pinStatus = PinPasswordStatus.ENABLE
            } else {
                pinStatus = PinPasswordStatus.FALSE
            }
            it.copy(pinStatus = pinStatus)
        }
    }

    fun onTapKeyNumber(password: String) = launch {
        _uiState.update {
            val updatedList = it.lPassWord.toMutableList()
            if (updatedList.size < it.lengthPassWord) {
                updatedList.add(password)
            }
            onConfirm(updatedList)
            it.copy(lPassWord = updatedList)
        }
    }

    private fun formatListToString(lPassWord: List<String>) : String {
        return lPassWord.joinToString("")
    }

    private fun checkPassWord(lPassWord: List<String>,password: String): Boolean {
        return formatListToString(lPassWord) == password
    }

}