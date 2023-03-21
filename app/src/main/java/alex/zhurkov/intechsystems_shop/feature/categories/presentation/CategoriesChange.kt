package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIStateChange
import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page

sealed class CategoriesChange : UIStateChange {
    data class PageLoadingChanged(val isLoading: Boolean) : CategoriesChange()
    data class RefreshChanged(val isRefreshing: Boolean) : CategoriesChange()
    data class PageLoaded(val data: Page<Category>?) : CategoriesChange()
    data class LastVisibleItemChanged(val id: String) : CategoriesChange()
    data class NetworkChanged(val isConnected: Boolean) : CategoriesChange()
}