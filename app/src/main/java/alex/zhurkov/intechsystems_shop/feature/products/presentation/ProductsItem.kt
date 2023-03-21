package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.domain.model.Product
import java.util.*

sealed class ProductsItem {
    abstract val id: Long

    data class Data(
        override val id: Long,
        val categoryId: String,
        val url: String,
        val name: String,
        val imageUrl: String,
        val listPrice: Double
    ) : ProductsItem()

    data class Loading(override val id: Long = UUID.randomUUID().timestamp()) : ProductsItem()
}

fun Product.toItem(imageUrlBase: String, imageFormat: String) = ProductsItem.Data(
    id = productId,
    categoryId = categoryId,
    url = url,
    imageUrl = "$imageUrlBase$imageUrl.$imageFormat",
    name = name,
    listPrice = listPrice,
)