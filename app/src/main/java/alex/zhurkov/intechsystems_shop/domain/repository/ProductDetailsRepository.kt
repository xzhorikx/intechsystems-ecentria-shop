package alex.zhurkov.intechsystems_shop.domain.repository

import alex.zhurkov.intechsystems_shop.domain.model.ProductDetails
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepository {
    fun observeDetails(productId: Long): Flow<ProductDetails>
}
