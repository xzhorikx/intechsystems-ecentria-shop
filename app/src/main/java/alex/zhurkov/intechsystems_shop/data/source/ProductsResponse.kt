package alex.zhurkov.intechsystems_shop.data.source

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("gridProducts") val gridProducts: GridProductsResponse,
)

data class GridProductsResponse(
    @SerializedName("elements") val products: List<ProductResponse>
)

data class ProductResponse(
    @SerializedName("url") val url: String?,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("shortName") val shortName: String?,
    @SerializedName("productId") val id: Long?,
    @SerializedName("listPrice") val listPrice: Any?,
    @SerializedName("primaryImage") val imageUrl: String?
)