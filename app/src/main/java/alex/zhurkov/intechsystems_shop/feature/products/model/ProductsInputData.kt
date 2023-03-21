package alex.zhurkov.intechsystems_shop.feature.products.model

import android.os.Bundle

data class ProductsInputData(
    val categoryId: String,
    val url: String
)


private const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"
private const val EXTRA_URL = "EXTRA_URL"

internal fun ProductsInputData.toBundle() = Bundle().apply {
    putString(EXTRA_CATEGORY_ID, categoryId)
    putString(EXTRA_URL, url)
}

internal fun Bundle?.getInputData() = ProductsInputData(
    categoryId = this?.getString(EXTRA_CATEGORY_ID, "").orEmpty(),
    url = this?.getString(EXTRA_URL, "").orEmpty()
)
