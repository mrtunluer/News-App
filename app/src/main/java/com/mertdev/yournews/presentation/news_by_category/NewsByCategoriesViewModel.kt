package com.mertdev.yournews.presentation.news_by_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.yournews.domain.usecase.GetNewsBySelectedCategories
import com.mertdev.yournews.helpers.Resource
import com.mertdev.yournews.presentation.common.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsByCategoriesViewModel @Inject constructor(
    private val getNewsBySelectedCategories: GetNewsBySelectedCategories
) : ViewModel() {

    private val _uiState: MutableStateFlow<DataState> = MutableStateFlow(DataState())
    val uiState: StateFlow<DataState> = _uiState

    init {
        fetchNewsBySelectedCategories()
    }

    fun fetchNewsBySelectedCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getNewsBySelectedCategories().collectLatest { resource ->
                _uiState.update {
                    when (resource) {
                        is Resource.Success -> uiState.value.copy(
                            articles = resource.data ?: emptyList(),
                            errorMessage = null,
                            isLoading = false
                        )

                        is Resource.Error -> uiState.value.copy(
                            articles = emptyList(),
                            errorMessage = resource.message.toString(),
                            isLoading = false
                        )

                        is Resource.Loading -> uiState.value.copy(
                            articles = emptyList(), errorMessage = null, isLoading = true
                        )
                    }
                }
            }
        }
    }

}