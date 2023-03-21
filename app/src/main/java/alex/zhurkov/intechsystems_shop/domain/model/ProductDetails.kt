package alex.zhurkov.intechsystems_shop.domain.model

data class ProductDetails(
    val productId: Long,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val description: String
)
