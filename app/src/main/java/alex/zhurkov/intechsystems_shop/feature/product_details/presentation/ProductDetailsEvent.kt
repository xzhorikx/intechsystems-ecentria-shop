package alex.zhurkov.intechsystems_shop.feature.product_details.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIEvent

sealed class ProductDetailsEvent : UIEvent {
    data class DisplayError(val e: Throwable) : ProductDetailsEvent()
    data class NetworkConnectionChanged(val isConnected: Boolean) : ProductDetailsEvent()
}