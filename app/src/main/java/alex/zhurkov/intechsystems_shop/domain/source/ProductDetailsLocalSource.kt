package alex.zhurkov.intechsystems_shop.domain.source

import alex.zhurkov.intechsystems_shop.domain.model.ProductDetails
import kotlinx.coroutines.flow.Flow

interface ProductDetailsLocalSource {
    suspend fun save(productDetails: ProductDetails)
    fun observeDetails(productId: Long): Flow<ProductDetails>
}
