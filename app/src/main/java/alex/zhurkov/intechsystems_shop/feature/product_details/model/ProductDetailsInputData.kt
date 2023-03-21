package alex.zhurkov.intechsystems_shop.feature.product_details.model

import android.os.Bundle

data class ProductDetailsInputData(
    val productId: Long,
)


private const val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"

internal fun ProductDetailsInputData.toBundle() = Bundle().apply {
    putLong(EXTRA_PRODUCT_ID, productId)
}

internal fun Bundle?.getInputData() = ProductDetailsInputData(
    productId = this?.getLong(EXTRA_PRODUCT_ID, 0) ?: 0,
)
