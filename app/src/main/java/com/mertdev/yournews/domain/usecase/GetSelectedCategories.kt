package com.mertdev.yournews.domain.usecase

import com.mertdev.yournews.domain.repo.CategoryRepository
import javax.inject.Inject

class GetSelectedCategories @Inject constructor(
    private val repo: CategoryRepository
) {
    suspend operator fun invoke(): List<String> {
        return repo.getSelectedCategories().filter { it.isNotEmpty() }
    }
}