package alex.zhurkov.intechsystems_shop.data.source

import com.google.gson.annotations.SerializedName

data class ProductDetailsResponse(
    @SerializedName("product_id") val productId: Long?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("short_name") val shortName: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("clean_description") val cleanDescription: String?,
    @SerializedName("meta_description") val metaDescription: String?,
    @SerializedName("list_price") val listPrice: Any?,
    @SerializedName("primary_image") val primaryImage: String?
)
