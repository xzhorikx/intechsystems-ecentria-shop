package alex.zhurkov.intechsystems_shop.data.source

import alex.zhurkov.intechsystems_shop.data.database.AppDatabase
import alex.zhurkov.intechsystems_shop.data.database.EntityMapper
import alex.zhurkov.intechsystems_shop.data.database.entity.ProductDetailsEntity
import alex.zhurkov.intechsystems_shop.domain.model.ProductDetails
import alex.zhurkov.intechsystems_shop.domain.source.ProductDetailsLocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class ProductDetailsLocalSourceImpl(
    private val database: AppDatabase,
    private val detailsMapper: EntityMapper<ProductDetails, ProductDetailsEntity>,
) : ProductDetailsLocalSource {
    override suspend fun save(productDetails: ProductDetails) =
        database.productDetailsDao().save(detailsMapper.toEntity(productDetails))

    override fun observeDetails(productId: Long): Flow<ProductDetails> =
        database.productDetailsDao().observeDetails(productId = productId)
            .filterNotNull().map(detailsMapper::toModel)
}