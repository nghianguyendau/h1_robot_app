package com.phenikaa.h1_robot_app.data.mapper

import com.phenikaa.h1_robot_app.data.mapper.base.BaseDataMapper
import com.phenikaa.h1_robot_app.data.model.DeckData
import com.phenikaa.h1_robot_app.data.model.SiteMonitoringResponse
import com.phenikaa.h1_robot_app.domain.entity.Deck
import com.phenikaa.h1_robot_app.domain.entity.SiteMonitoring

class SiteMonitoringResponseMapper : BaseDataMapper<SiteMonitoringResponse, SiteMonitoring> {
    override fun mapToEntity(data: SiteMonitoringResponse?): SiteMonitoring {
        return SiteMonitoring(
            time = data?.data?.time ?: "",
            dir = data?.data?.dir ?: "",
            coll = data?.data?.coll ?: "",
            movingState = data?.data?.movingState ?: "",
            area = data?.data?.area ?: 0,
            cur = data?.data?.cur ?: -1,
            adv = data?.data?.adv ?: 0,
            door = data?.data?.door ?: false,
            liftSide = data?.data?.liftSide ?: -1,
            state = data?.data?.state ?: "",
            landing = data?.data?.landing ?: -1,
            liftMode = data?.data?.liftMode ?: -1,
            nominalSpeed = data?.data?.nominalSpeed ?: -1,
            decks = data?.data?.decks?.map { it.toEntity() } ?: emptyList()
        )
    }

    private fun DeckData.toEntity(): Deck {
        return Deck(area = area)
    }
}