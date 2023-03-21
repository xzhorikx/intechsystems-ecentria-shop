package alex.zhurkov.intechsystems_shop.domain.source

import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page
import kotlinx.coroutines.flow.Flow

interface CategoryLocalSource {
    suspend fun getCategoryPage(page: Int, limit: Int): Page<Category>?
    suspend fun saveCategories(categories: List<Category>)
    fun observeCategory(id: String): Flow<Category>
}
