package com.avirajsharma.booko.presentation.screens.settingsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.SingletonImageLoader
import com.avirajsharma.booko.domain.repository.BookRepository
import com.avirajsharma.booko.domain.repository.SettingsRepository
import com.avirajsharma.booko.domain.repository.ThemeSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    private val bookRepository: BookRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val themeSetting: StateFlow<ThemeSetting> = repository.themeSetting
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeSetting.SYSTEM
        )

    val downloadedBooksCount: StateFlow<Int> = bookRepository.getAllBooks()
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    private val _cacheSize = MutableStateFlow("0 MB")
    val cacheSize: StateFlow<String> = _cacheSize.asStateFlow()

    init {
        updateCacheSize()
    }

    fun setTheme(theme: ThemeSetting) {
        viewModelScope.launch {
            repository.setThemeSetting(theme)
        }
    }

    fun clearImageCache() {
        viewModelScope.launch {
            val imageLoader = SingletonImageLoader.get(context)
            imageLoader.diskCache?.clear()
            imageLoader.memoryCache?.clear()
            updateCacheSize()
        }
    }

    private fun updateCacheSize() {
        viewModelScope.launch {
            val imageLoader = SingletonImageLoader.get(context)
            val sizeBytes = imageLoader.diskCache?.size ?: 0L
            val sizeMb = sizeBytes / (1024 * 1024)
            _cacheSize.value = "$sizeMb MB"
        }
    }
}
