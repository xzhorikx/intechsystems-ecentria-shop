package alex.zhurkov.intechsystems_shop.domain.repository

import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page

interface CategoriesRepository {
    suspend fun getCategoryPage(page: Int, skipCache: Boolean): Page<Category>?
}