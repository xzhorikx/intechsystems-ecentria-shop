package alex.zhurkov.intechsystems_shop.data.source

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("url") val url: String
)