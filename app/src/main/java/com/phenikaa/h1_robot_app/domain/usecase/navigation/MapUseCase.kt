package com.phenikaa.h1_robot_app.domain.usecase.navigation

import com.csjbot.coshandler.listener.OnMapListListener
import com.csjbot.coshandler.listener.OnMapListener
import com.phenikaa.h1_robot_app.domain.repository.NavigationRepository
import javax.inject.Inject

class MapUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) {
    suspend fun loadDefaultMap() {
        navigationRepository.loadMap()
    }

    suspend fun loadMapByName(name: String) {
        navigationRepository.loadMap(name)
    }

    suspend fun loadMapWithListener(name: String, listener: OnMapListener) {
        navigationRepository.loadMap(name, listener)
    }

    suspend fun loadMapToPosition(name: String, x: Float, y: Float, rotation: Float, listener: OnMapListener?) {
        navigationRepository.loadMap(name, x, y, rotation, listener)
    }

    suspend operator fun invoke(): List<String> {
        return navigationRepository.getMapList()
    }
}