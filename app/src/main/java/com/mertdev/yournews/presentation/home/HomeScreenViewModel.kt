package com.mertdev.yournews.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.yournews.domain.usecase.GetTopHeadlines
import com.mertdev.yournews.domain.usecase.SearchNews
import com.mertdev.yournews.helpers.Resource
import com.mertdev.yournews.presentation.common.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getTopHeadlines: GetTopHeadlines, private val searchNews: SearchNews
) : ViewModel() {

    private val _uiState: MutableStateFlow<DataState> = MutableStateFlow(DataState())
    val uiState: StateFlow<DataState> = _uiState

    private var job: Job? = null

    init {
        getBreakingNews()
    }

    fun getBreakingNews() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getTopHeadlines().collectLatest { resource ->
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

    fun getSearchedNews(query: String) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            delay(300) // debounce
            searchNews(query).collectLatest { resource ->
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