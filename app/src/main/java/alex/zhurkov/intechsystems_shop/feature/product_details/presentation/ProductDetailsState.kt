package alex.zhurkov.intechsystems_shop.feature.product_details.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIState
import alex.zhurkov.intechsystems_shop.domain.model.ProductDetails

data class ProductDetailsState(
    val productId: Long,
    val productDetails: ProductDetails?,
    val isNetworkConnected: Boolean?
) : UIState {

    companion object {
        fun getInitial(productId: Long) = ProductDetailsState(
            productId = productId,
            isNetworkConnected = null,
            productDetails = null
        )
    }
}
