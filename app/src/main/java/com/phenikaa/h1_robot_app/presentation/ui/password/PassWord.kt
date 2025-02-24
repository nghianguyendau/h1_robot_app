package com.phenikaa.h1_robot_app.presentation.ui.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phenikaa.h1_robot_app.presentation.common_view.CommonScaffold
import com.phenikaa.h1_robot_app.presentation.ui.password.components.PinPutComponent
import com.phenikaa.h1_robot_app.presentation.navigation.Screen
import com.phenikaa.h1_robot_app.presentation.share_view.KeyBoard
import com.phenikaa.h1_robot_app.utils.collectAsEffect
import com.phenikaa.h1_robot_app.R
import com.phenikaa.h1_robot_app.domain.entity.enum.PinPasswordStatus
import com.phenikaa.h1_robot_app.presentation.theme.AppColor


@Composable
fun PassWordPage(
    navController: NavHostController,
    viewModel: PassWordViewModel = hiltViewModel()
) {
    viewModel.navigationState.collectAsEffect { navigationState ->
        when (navigationState) {
            is PassWordNavigationState.NavigationBar -> navController.navigate(Screen.ElevatorScreen.route)
        }
    }

    val state by viewModel.state.collectAsState()
    PassWordScreen(state, navController,
        onTapRemove = { key ->
            viewModel.onTapRemove(key)
        },
        onTapKeyNumber = { key ->
            viewModel.onTapKeyNumber(key)
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassWordScreen(
    state: PassWordState,
    appNavController: NavHostController,
    onTapRemove: (lPassWord: List<String>) -> Unit,
    onTapKeyNumber: (number: String) -> Unit
) {
    CommonScaffold(
        content = { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize(1f)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.type_pass_word), fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.blackColor,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                PinPutComponent(
                    state.lPassWord,
                    state.lengthPassWord,
                    state.pinStatus
                )
                Spacer(modifier = Modifier.height(50.dp))
                if (state.pinStatus == PinPasswordStatus.FALSE) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.wrong_password),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColor.red400,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                KeyBoard(
                    onTapRemove = {
                        onTapRemove(state.lPassWord)
                    },
                    onTapKeyNumber = { key ->
                        onTapKeyNumber(key)
                    }
                )
            }

        },
        navController = appNavController,
    )
}