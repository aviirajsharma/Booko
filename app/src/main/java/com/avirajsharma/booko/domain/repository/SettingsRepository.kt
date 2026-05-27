package com.avirajsharma.booko.domain.repository

import kotlinx.coroutines.flow.Flow

enum class ThemeSetting {
    LIGHT, DARK, SYSTEM
}

interface SettingsRepository {
    val themeSetting: Flow<ThemeSetting>
    suspend fun setThemeSetting(theme: ThemeSetting)
}
