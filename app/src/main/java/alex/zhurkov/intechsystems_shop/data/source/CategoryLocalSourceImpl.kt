package alex.zhurkov.intechsystems_shop.data.source

import alex.zhurkov.intechsystems_shop.data.database.AppDatabase
import alex.zhurkov.intechsystems_shop.data.database.EntityMapper
import alex.zhurkov.intechsystems_shop.data.database.entity.CategoryEntity
import alex.zhurkov.intechsystems_shop.domain.config.ConfigSource
import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.source.CategoryLocalSource

class CategoryLocalSourceImpl(
    private val database: AppDatabase,
    private val categoryMapper: EntityMapper<Category, CategoryEntity>,
    private val configSource: ConfigSource
) : CategoryLocalSource {
    override suspend fun getCategoryPage(page: Int, limit: Int): Page<Category>? {
        if (page <= 0) return null
        val offset = (page - 1) * configSource.pageSize
        val categories = database.categoryDao().getCategories(
            limit = limit, offset = offset
        ).map(categoryMapper::toModel).takeIf { it.isNotEmpty() } ?: return null

        return Page(items = categories, pageId = page, isLastPage = categories.size < limit)
    }

    override suspend fun saveCategories(categories: List<Category>) =
        database.categoryDao().save(categories.map(categoryMapper::toEntity))
}