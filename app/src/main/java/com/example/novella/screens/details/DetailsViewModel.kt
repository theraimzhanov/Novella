package com.example.novella.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novella.data.Resource
import com.example.novella.model.Item
import com.example.novella.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {

    suspend fun getBookInfo(bookId: String): Resource<Item>{
        return repository.getBookInfo(bookId)
    }
}


