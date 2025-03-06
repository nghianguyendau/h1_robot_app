package com.phenikaa.h1_robot_app.domain.usecase.phonecall

import com.phenikaa.h1_robot_app.data.repository.PhoneCallRepository
import com.phenikaa.h1_robot_app.domain.entity.LoraMessage
import javax.inject.Inject

class SendLoraMessageUseCase @Inject constructor(
    private val phoneCallRepository: PhoneCallRepository
) {
    operator fun invoke(loraMessage: String) {
        val message = LoraMessage(
            event = "send-lora",
            message = loraMessage
        )
        phoneCallRepository.sendLoraMessage(message)
    }
}