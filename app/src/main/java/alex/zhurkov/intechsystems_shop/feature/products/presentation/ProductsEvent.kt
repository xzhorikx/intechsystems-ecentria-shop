package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIEvent

sealed class ProductsEvent : UIEvent {
    data class DisplayError(val e: Throwable) : ProductsEvent()
    data class NetworkConnectionChanged(val isConnected: Boolean) : ProductsEvent()
}