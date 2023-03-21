package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.domain.model.Page

class ProductsReducer : Reducer<ProductsState, ProductsChange> {
    override fun reduce(state: ProductsState, change: ProductsChange): ProductsState {
        return when (change) {
            is ProductsChange.PageLoaded -> when (change.data) {
                null -> {
                    // When new page is null, it's an indicator that all pages were loaded
                    val lastPage = state.pages.lastOrNull() ?: Page(
                        pageId = state.nextPage ?: state.initialPageId,
                        isLastPage = true,
                        items = emptyList()
                    )
                    state.copy(pages = state.pages + lastPage.copy(isLastPage = true))
                }
                else -> state.copy(pages = state.pages + change.data)
            }
            is ProductsChange.PageLoadingChanged -> state.copy(isPageLoading = change.isLoading)
            is ProductsChange.RefreshChanged -> state.copy(isRefreshing = change.isRefreshing)
            is ProductsChange.LastVisibleItemChanged -> state.copy(lastVisibleItemId = change.id)
            is ProductsChange.NetworkChanged -> state.copy(isNetworkConnected = change.isConnected)
            is ProductsChange.CategoryChanged -> state.copy(category = change.data)
        }
    }
}
