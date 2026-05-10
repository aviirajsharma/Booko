package com.avirajsharma.booko.presentation.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.domain.usecases.GetBooksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val bookUseCase: GetBooksUseCase by lazy {
        GetBooksUseCase()
    }

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
                    _uiState.value = HomeUiState.Error(error.message ?:"Unknown Error")
                }
            }
        }
    }

}


//data class UiState(
//    val isLoading: Boolean = false,
//    val error: String? = "",
//    val data: BooksResponse? = null
//)

sealed interface HomeUiState{
    data object Loading : HomeUiState
    data class Error(val error: String): HomeUiState
    data class Success(val data: BooksResponse) : HomeUiState

}