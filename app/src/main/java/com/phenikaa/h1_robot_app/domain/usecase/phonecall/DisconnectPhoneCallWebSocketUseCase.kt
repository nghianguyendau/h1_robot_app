package com.phenikaa.h1_robot_app.domain.usecase.phonecall

import com.phenikaa.h1_robot_app.data.repository.PhoneCallRepository
import javax.inject.Inject

class DisconnectPhoneCallWebSocketUseCase @Inject constructor(
    private val phoneCallRepository: PhoneCallRepository
) {
    operator fun invoke() {
        phoneCallRepository.disconnect()
    }
}