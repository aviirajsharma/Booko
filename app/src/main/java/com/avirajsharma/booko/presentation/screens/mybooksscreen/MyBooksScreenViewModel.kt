package com.avirajsharma.booko.presentation.screens.mybooksscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.booko.data.local.BookEntity
import com.avirajsharma.booko.domain.usecases.DeleteBookUseCase
import com.avirajsharma.booko.domain.usecases.GetAllBooksUseCase
import com.avirajsharma.booko.domain.usecases.OpenPDFUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyBooksScreenViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val openPDFUseCase: OpenPDFUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyBooksState>(MyBooksState.Loading)
    val uiState: StateFlow<MyBooksState> = _uiState.asStateFlow()

    init {
        getAllBooks()
    }


    fun getAllBooks() {
        viewModelScope.launch {
            _uiState.value = MyBooksState.Loading
            getAllBooksUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.value = MyBooksState.Success(data)
                }.onFailure {
                    _uiState.value = MyBooksState.Error(it.message.toString())
                }
            }
        }
    }

    fun openPdf(context: Context, filePath: String) {
        openPDFUseCase.invoke(context, filePath)
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            deleteBookUseCase(bookId)
        }
    }

}

sealed interface MyBooksState {
    data object Loading : MyBooksState
    data class Error(val error: String) : MyBooksState
    data class Success(val data: List<BookEntity>) : MyBooksState
}