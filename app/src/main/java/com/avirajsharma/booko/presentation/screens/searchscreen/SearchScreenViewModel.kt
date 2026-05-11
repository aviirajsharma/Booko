package com.avirajsharma.booko.presentation.screens.searchscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.domain.usecases.SearchBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchBookUseCase: SearchBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun searchQuery(query: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            searchBookUseCase.invoke(query).collect {
                it.onSuccess { data ->
                    _uiState.value = SearchUiState.Success(data)
                }.onFailure { error ->
                    _uiState.value = SearchUiState.Error(error.message ?: "Unknown Error")
                }
            }
        }
    }
}


sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(val result: BooksResponse) : SearchUiState
    data class Error(val error: String) : SearchUiState
}