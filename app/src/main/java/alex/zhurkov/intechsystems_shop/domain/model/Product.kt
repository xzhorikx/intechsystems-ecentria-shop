package alex.zhurkov.intechsystems_shop.domain.model

data class Product(
    val productId: Long,
    val categoryId: String,
    val url: String,
    val name: String,
    val imageUrl: String,
    val listPrice: Double
)
