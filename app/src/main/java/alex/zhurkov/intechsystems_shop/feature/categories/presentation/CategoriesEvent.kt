package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIEvent

sealed class CategoriesEvent : UIEvent {
    data class DisplayError(val e: Throwable) : CategoriesEvent()
    data class NetworkConnectionChanged(val isConnected: Boolean) : CategoriesEvent()
}