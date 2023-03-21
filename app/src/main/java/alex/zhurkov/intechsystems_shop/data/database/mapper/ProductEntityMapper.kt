package alex.zhurkov.intechsystems_shop.data.database.mapper

import alex.zhurkov.intechsystems_shop.data.database.EntityMapper
import alex.zhurkov.intechsystems_shop.data.database.entity.ProductEntity
import alex.zhurkov.intechsystems_shop.domain.model.Product

class ProductEntityMapper : EntityMapper<Product, ProductEntity> {
    override fun toModel(entity: ProductEntity): Product = Product(
        productId = entity.productId,
        categoryId = entity.categoryId,
        url = entity.url,
        imageUrl = entity.imageUrl,
        name = entity.name,
        listPrice = entity.listPrice,
    )

    override fun toEntity(model: Product): ProductEntity = ProductEntity(
        productId = model.productId,
        categoryId = model.categoryId,
        url = model.url,
        imageUrl = model.imageUrl,
        name = model.name,
        listPrice = model.listPrice,
    )
}
