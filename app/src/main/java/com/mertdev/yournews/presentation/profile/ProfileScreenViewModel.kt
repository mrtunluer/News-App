package com.mertdev.yournews.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.yournews.domain.usecase.GetSelectedCategories
import com.mertdev.yournews.domain.usecase.SaveSelectedCategories
import com.mertdev.yournews.presentation.common.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val saveSelectedCategories: SaveSelectedCategories,
    private val getSelectedCategories: GetSelectedCategories
) : ViewModel() {

    private val _uiState: MutableStateFlow<DataState> = MutableStateFlow(DataState())
    val uiState: StateFlow<DataState> = _uiState

    val saveEvent: SharedFlow<SaveSelectedCategories.SaveEvent> = saveSelectedCategories.eventFlow

    init {
        fetchSelectedCategories()
    }

    fun saveCategories(categories: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSelectedCategories(categories)
        }
    }

    private fun fetchSelectedCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedCategories = getSelectedCategories()
            _uiState.update {
                uiState.value.copy(
                    selectedCategories = selectedCategories
                )
            }
        }
    }

}