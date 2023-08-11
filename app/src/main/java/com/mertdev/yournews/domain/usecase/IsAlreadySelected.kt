package com.mertdev.yournews.domain.usecase

import com.mertdev.yournews.domain.repo.CategoryRepository
import javax.inject.Inject

class IsAlreadySelected @Inject constructor(
    private val repo: CategoryRepository
) {
    suspend operator fun invoke(): Boolean {
        return repo.isAlreadySelected()
    }
}