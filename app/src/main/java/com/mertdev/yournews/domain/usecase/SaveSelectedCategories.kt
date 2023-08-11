package com.mertdev.yournews.domain.usecase

import com.mertdev.yournews.domain.repo.CategoryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class SaveSelectedCategories @Inject constructor(
    private val repo: CategoryRepository
) {
    sealed class SaveEvent {
        object GoHome : SaveEvent()
        object ShowMessage: SaveEvent()
    }

    private val _eventFlow = MutableSharedFlow<SaveEvent>()
    val eventFlow: SharedFlow<SaveEvent> = _eventFlow

    suspend operator fun invoke(categories: List<String>) {
        if (categories.isNotEmpty()) {
            repo.saveSelectedCategories(categories)
            _eventFlow.emit(SaveEvent.GoHome)
        }else{
            _eventFlow.emit(SaveEvent.ShowMessage)
        }
    }
}