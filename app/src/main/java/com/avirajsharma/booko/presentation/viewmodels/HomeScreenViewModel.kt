package com.avirajsharma.booko.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.domain.usecases.GetBooksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val bookUseCase: GetBooksUseCase by lazy {
        GetBooksUseCase()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getBooks()
    }

    fun getBooks() {
        _uiState.update { UiState(isLoading = true) }
        viewModelScope.launch {
            bookUseCase.invoke().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { UiState(data = data, isLoading = false) }
                }.onFailure { error ->
                    _uiState.update { UiState(error = error.message.toString()) }
                }
            }
        }
    }

}


data class UiState(
    val isLoading: Boolean = false,
    val error: String ?= "",
    val data: BooksResponse? = null
)