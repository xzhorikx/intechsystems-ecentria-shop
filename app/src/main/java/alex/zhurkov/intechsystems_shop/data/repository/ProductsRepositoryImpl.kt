package alex.zhurkov.intechsystems_shop.data.repository

import alex.zhurkov.intechsystems_shop.data.source.ProductRemoteSource
import alex.zhurkov.intechsystems_shop.data.source.ProductResponse
import alex.zhurkov.intechsystems_shop.domain.config.ConfigSource
import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.model.Product
import alex.zhurkov.intechsystems_shop.domain.repository.ProductLocalSource
import alex.zhurkov.intechsystems_shop.domain.repository.ProductsRepository

class ProductsRepositoryImpl(
    private val configSource: ConfigSource,
    private val localSource: ProductLocalSource,
    private val remoteSource: ProductRemoteSource
) : ProductsRepository {
    override suspend fun getProductsPage(
        categoryUrl: String,
        categoryId: String,
        page: Int,
        skipCache: Boolean
    ): Page<Product>? {
        val limit = configSource.pageSize
        return when (skipCache) {
            true -> getRemoteCategories(
                categoryUrl = categoryUrl,
                categoryId = categoryId,
                pageId = page,
                limit = limit
            )
            false -> {
                val local =
                    localSource.getProductPage(categoryId = categoryId, page = page, limit = limit)
                // If page is not found locally, syncing with remote
                return when (local) {
                    null -> getRemoteCategories(
                        categoryId = categoryId,
                        categoryUrl = categoryUrl,
                        pageId = page,
                        limit = limit
                    )
                    else -> local
                }
            }
        }
    }

    private suspend fun getRemoteCategories(
        categoryId: String,
        categoryUrl: String,
        pageId: Int,
        limit: Int
    ): Page<Product>? {
        val response = remoteSource.getProducts(categoryId = categoryUrl)
        localSource.saveProducts(response.gridProducts.products.map { it.toModel(categoryId) })
        return localSource.getProductPage(categoryId = categoryId, page = pageId, limit = limit)
    }

    private fun ProductResponse.toModel(categoryId: String) = Product(
        categoryId = categoryId,
        url = url ?: "no_url",
        productId = id ?: -1,
        name = shortName ?: fullName ?: "no_name",
        imageUrl = imageUrl ?: "no_image",
        listPrice = runCatching { listPrice as Double }.getOrElse { runCatching { (listPrice as Long).toDouble() }.getOrElse { -1.0 } },
    )
}