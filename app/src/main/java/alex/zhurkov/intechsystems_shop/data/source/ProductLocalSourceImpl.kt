package alex.zhurkov.intechsystems_shop.data.source

import alex.zhurkov.intechsystems_shop.data.database.AppDatabase
import alex.zhurkov.intechsystems_shop.data.database.EntityMapper
import alex.zhurkov.intechsystems_shop.data.database.entity.ProductEntity
import alex.zhurkov.intechsystems_shop.domain.config.ConfigSource
import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.model.Product
import alex.zhurkov.intechsystems_shop.domain.repository.ProductLocalSource

class ProductLocalSourceImpl(
    private val database: AppDatabase,
    private val productMapper: EntityMapper<Product, ProductEntity>,
    private val configSource: ConfigSource
) : ProductLocalSource {
    override suspend fun getProductPage(categoryId: String, page: Int, limit: Int): Page<Product>? {
        if (page <= 0) return null
        val offset = (page - 1) * configSource.pageSize
        val categories = database.productDao()
            .getProducts(categoryId = categoryId, limit = limit, offset = offset)
            .map(productMapper::toModel).takeIf { it.isNotEmpty() } ?: return null

        return Page(items = categories, pageId = page, isLastPage = categories.size < limit)
    }

    override suspend fun saveProducts(products: List<Product>) =
        database.productDao().save(products.map(productMapper::toEntity))
}
