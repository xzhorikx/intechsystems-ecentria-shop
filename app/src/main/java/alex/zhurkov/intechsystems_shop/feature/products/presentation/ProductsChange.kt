package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIStateChange
import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.model.Product

sealed class ProductsChange : UIStateChange {
    data class PageLoadingChanged(val isLoading: Boolean) : ProductsChange()
    data class RefreshChanged(val isRefreshing: Boolean) : ProductsChange()
    data class PageLoaded(val data: Page<Product>?) : ProductsChange()
    data class LastVisibleItemChanged(val id: Long) : ProductsChange()
    data class NetworkChanged(val isConnected: Boolean) : ProductsChange()
    data class CategoryChanged(val data: Category) : ProductsChange()
}