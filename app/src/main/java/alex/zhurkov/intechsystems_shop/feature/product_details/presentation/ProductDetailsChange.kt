package alex.zhurkov.intechsystems_shop.feature.product_details.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIStateChange
import alex.zhurkov.intechsystems_shop.domain.model.ProductDetails

sealed class ProductDetailsChange : UIStateChange {
    data class NetworkChanged(val isConnected: Boolean) : ProductDetailsChange()
    data class ProductDetailsChanged(val data: ProductDetails) : ProductDetailsChange()
}