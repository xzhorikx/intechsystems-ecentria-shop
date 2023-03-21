package alex.zhurkov.intechsystems_shop.domain.repository

import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.model.Product

interface ProductsRepository {
    suspend fun getProductsPage(
        categoryUrl: String,
        categoryId: String,
        page: Int,
        skipCache: Boolean
    ): Page<Product>?
}
