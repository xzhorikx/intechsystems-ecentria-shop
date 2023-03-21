package alex.zhurkov.intechsystems_shop.data.repository

import alex.zhurkov.intechsystems_shop.data.source.CategoryRemoteSource
import alex.zhurkov.intechsystems_shop.data.source.CategoryResponse
import alex.zhurkov.intechsystems_shop.domain.config.ConfigSource
import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.repository.CategoriesRepository
import alex.zhurkov.intechsystems_shop.domain.source.CategoryLocalSource

class CategoryRepositoryImpl(
    private val configSource: ConfigSource,
    private val localSource: CategoryLocalSource,
    private val remoteSource: CategoryRemoteSource,
) : CategoriesRepository {
    override suspend fun getCategoryPage(page: Int, skipCache: Boolean): Page<Category>? {
        val limit = configSource.pageSize
        return when (skipCache) {
            true -> getRemoteCategories(pageId = page, limit = limit)
            false -> {
                val local = localSource.getCategoryPage(page = page, limit = limit)
                // If page is not found locally, syncing with remote
                return when (local) {
                    null -> getRemoteCategories(pageId = page, limit = limit)
                    else -> local
                }
            }
        }
    }

    private suspend fun getRemoteCategories(pageId: Int, limit: Int): Page<Category>? {
        val response = remoteSource.getCategories()
        localSource.saveCategories(response.map { it.toModel() })
        return localSource.getCategoryPage(page = pageId, limit = limit)
    }

    private fun CategoryResponse.toModel() = Category(
        categoryId = categoryId,
        fullName = fullName,
        url = url
    )
}