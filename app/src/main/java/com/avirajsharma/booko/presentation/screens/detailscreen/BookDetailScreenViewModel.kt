package com.avirajsharma.booko.presentation.screens.detailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.booko.data.model.BookDetailResponse
import com.avirajsharma.booko.domain.usecases.DownloadBookUseCase
import com.avirajsharma.booko.domain.usecases.GetBookDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class BookDetailScreenViewModel @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailUseCase,
    private val downloadBookUseCase: DownloadBookUseCase
) :
    ViewModel() {
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()


    fun getBookDetail(bookId: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            getBookDetailUseCase(bookId).collect { result ->
                result.onSuccess { data ->
                    _uiState.value = DetailUiState.Success(data)
                }.onFailure {
                    _uiState.value = DetailUiState.Error(it.message.toString())
                }
            }
        }
    }

    fun downloadBook(book: BookDetailResponse) {
        viewModelScope.launch {
            downloadBookUseCase(book)
        }
    }
}


sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Error(val error: String) : DetailUiState
    data class Success(val data: BookDetailResponse) : DetailUiState
}