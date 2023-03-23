package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.model.Product

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
                else -> {
                    val page =
                        change.data.copy(items = change.data.items.distinctBy(Product::productId))
                    val pages = (state.pages + page).distinctBy(Page<Product>::pageId)
                    state.copy(pages = pages)
                }
            }
            is ProductsChange.PageLoadingChanged -> state.copy(isPageLoading = change.isLoading)
            is ProductsChange.RefreshChanged -> state.copy(isRefreshing = change.isRefreshing)
            is ProductsChange.LastVisibleItemChanged -> state.copy(lastVisibleItemId = change.id)
            is ProductsChange.NetworkChanged -> state.copy(isNetworkConnected = change.isConnected)
            is ProductsChange.CategoryChanged -> state.copy(category = change.data)
        }
    }
}
