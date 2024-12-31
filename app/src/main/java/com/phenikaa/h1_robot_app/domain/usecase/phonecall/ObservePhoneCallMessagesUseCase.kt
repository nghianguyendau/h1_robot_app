package com.phenikaa.h1_robot_app.domain.usecase.phonecall

import com.phenikaa.h1_robot_app.data.repository.PhoneCallRepository
import com.phenikaa.h1_robot_app.domain.model.PhoneCallResponseMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePhoneCallMessagesUseCase @Inject constructor(
    private val phoneCallRepository: PhoneCallRepository
) {
    operator fun invoke(): Flow<PhoneCallResponseMessage> {
        return phoneCallRepository.receivePhoneCallMessages()
    }
}