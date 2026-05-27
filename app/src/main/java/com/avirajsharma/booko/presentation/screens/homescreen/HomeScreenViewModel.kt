package com.avirajsharma.booko.presentation.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.domain.usecases.GetBooksUseCase
import com.avirajsharma.booko.domain.usecases.SearchBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val bookUseCase: GetBooksUseCase,
    private val searchBookUseCase: SearchBookUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            bookUseCase.invoke().collect { result ->
                result.onSuccess { data ->
                    _uiState.value = HomeUiState.Success(data)
                }.onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Unknown Error")
                }
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            searchBookUseCase.invoke(query).collect { result ->
                result.onSuccess { data ->
                    _uiState.value = HomeUiState.Success(data)
                }.onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Unknown Error")
                }
            }
        }
    }

}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Error(val error: String) : HomeUiState
    data class Success(val data: BooksResponse) : HomeUiState
}
