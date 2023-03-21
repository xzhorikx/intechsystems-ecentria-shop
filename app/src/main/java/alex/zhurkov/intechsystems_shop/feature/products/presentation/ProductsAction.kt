package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIAction

sealed class ProductsAction : UIAction {
    object Refresh : ProductsAction()
    data class LastVisibleItemChanged(val id: Long) : ProductsAction()
}