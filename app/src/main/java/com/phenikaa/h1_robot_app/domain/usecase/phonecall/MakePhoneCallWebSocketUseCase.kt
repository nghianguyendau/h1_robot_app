package com.phenikaa.h1_robot_app.domain.usecase.phonecall

import com.phenikaa.h1_robot_app.data.repository.PhoneCallRepository
import com.phenikaa.h1_robot_app.domain.model.PhoneCallMessage
import javax.inject.Inject

class MakePhoneCallUseCase @Inject constructor(
    private val phoneCallRepository: PhoneCallRepository
) {
    suspend operator fun invoke(phoneNumber: String) {
        val message = PhoneCallMessage(
            event = "call",
            number = phoneNumber
        )
        phoneCallRepository.sendPhoneCallMessage(message)
    }
}