package alex.zhurkov.intechsystems_shop.domain.repository

import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.model.Product

interface ProductLocalSource {
    suspend fun getProductPage(categoryId: String, page: Int, limit: Int): Page<Product>?
    suspend fun saveProducts(products: List<Product>)
}
