package alex.zhurkov.intechsystems_shop.feature.product_details.presentation

import alex.zhurkov.intechsystems_shop.domain.model.ProductDetails
import androidx.core.text.HtmlCompat
import java.util.*

sealed class ProductDetailsItem {
    abstract val id: Long

    data class Data(
        override val id: Long,
        val name: String,
        val imageUrl: String,
        val price: Double,
        val description: String
    ) : ProductDetailsItem()

    data class Loading(override val id: Long = UUID.randomUUID().hashCode().toLong()) :
        ProductDetailsItem()
}

fun ProductDetails.toItem(imageUrlBase: String, imageFormat: String) = ProductDetailsItem.Data(
    id = productId,
    imageUrl = "$imageUrlBase$imageUrl.$imageFormat",
    name = name,
    price = price,
    description = when (description.isEmpty()) {
        true -> ""
        else -> HtmlCompat.fromHtml(description, 0).toString()
    }
)