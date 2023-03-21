package alex.zhurkov.intechsystems_shop.domain.repository

import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    suspend fun getCategoryPage(page: Int, skipCache: Boolean): Page<Category>?
    fun observeCategory(id: String): Flow<Category>
}