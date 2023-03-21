package alex.zhurkov.intechsystems_shop.data.repository

import alex.zhurkov.intechsystems_shop.data.source.ProductDetailsRemoteSource
import alex.zhurkov.intechsystems_shop.data.source.ProductDetailsResponse
import alex.zhurkov.intechsystems_shop.domain.model.ProductDetails
import alex.zhurkov.intechsystems_shop.domain.repository.ProductDetailsRepository
import alex.zhurkov.intechsystems_shop.domain.source.ProductDetailsLocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge

class ProductDetailsRepositoryImpl(
    private val localSource: ProductDetailsLocalSource,
    private val remoteSource: ProductDetailsRemoteSource,
) : ProductDetailsRepository {
    override fun observeDetails(productId: Long): Flow<ProductDetails> {
        val remote = flow {
            val productDetails =
                remoteSource.getProductDetails(productId = productId).toProductDetails()
            localSource.save(productDetails)
            emit(productDetails)
        }
        val local = localSource.observeDetails(productId = productId)
        return merge(local, remote)
    }
}

private fun ProductDetailsResponse.toProductDetails() = ProductDetails(
    productId = productId ?: 0,
    name = shortName ?: fullName ?: "no_name",
    imageUrl = primaryImage ?: "no_image",
    price = runCatching { listPrice as Double }.getOrElse { runCatching { (listPrice as Long).toDouble() }.getOrElse { -1.0 } },
    description = description.orEmpty()
)