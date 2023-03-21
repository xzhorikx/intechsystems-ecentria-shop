package alex.zhurkov.intechsystems_shop.data.database.mapper

import alex.zhurkov.intechsystems_shop.data.database.EntityMapper
import alex.zhurkov.intechsystems_shop.data.database.entity.ProductDetailsEntity
import alex.zhurkov.intechsystems_shop.domain.model.ProductDetails

class ProductDetailsEntityMapper : EntityMapper<ProductDetails, ProductDetailsEntity> {
    override fun toModel(entity: ProductDetailsEntity): ProductDetails = ProductDetails(
        productId = entity.productId,
        name = entity.name,
        imageUrl = entity.imageUrl,
        price = entity.price,
        description = entity.description
    )

    override fun toEntity(model: ProductDetails): ProductDetailsEntity = ProductDetailsEntity(
        productId = model.productId,
        name = model.name,
        imageUrl = model.imageUrl,
        price = model.price,
        description = model.description
    )
}
