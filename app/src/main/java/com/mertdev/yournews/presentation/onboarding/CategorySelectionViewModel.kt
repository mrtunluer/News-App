package com.mertdev.yournews.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.yournews.domain.usecase.IsAlreadySelected
import com.mertdev.yournews.domain.usecase.SaveSelectedCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySelectionViewModel @Inject constructor(
    private val saveSelectedCategories: SaveSelectedCategories,
    private val isAlreadySelected: IsAlreadySelected
) : ViewModel() {

    sealed class SelectedState {
        object AlreadySelected : SelectedState()
        object NotSelected : SelectedState()
        object Loading: SelectedState()
    }

    val saveEvent: SharedFlow<SaveSelectedCategories.SaveEvent> = saveSelectedCategories.eventFlow

    private val _selectedState = MutableStateFlow<SelectedState>(SelectedState.Loading)
    val selectedState: StateFlow<SelectedState> = _selectedState

    init {
        isPreSelected()
    }

    fun saveCategories(categories: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSelectedCategories(categories)
        }
    }

    private fun isPreSelected() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isAlreadySelected()) _selectedState.value = SelectedState.AlreadySelected
            else _selectedState.value = SelectedState.NotSelected
        }
    }

}