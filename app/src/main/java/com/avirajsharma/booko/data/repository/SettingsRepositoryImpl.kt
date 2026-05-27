package com.avirajsharma.booko.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.avirajsharma.booko.domain.repository.SettingsRepository
import com.avirajsharma.booko.domain.repository.ThemeSetting
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private val themeKey = stringPreferencesKey("theme_setting")

    override val themeSetting: Flow<ThemeSetting> = context.dataStore.data
        .map { preferences ->
            val themeName = preferences[themeKey] ?: ThemeSetting.SYSTEM.name
            ThemeSetting.valueOf(themeName)
        }

    override suspend fun setThemeSetting(theme: ThemeSetting) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }
}
