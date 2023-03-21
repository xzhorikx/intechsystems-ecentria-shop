package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.domain.model.Page

class CategoriesReducer : Reducer<CategoriesState, CategoriesChange> {
    override fun reduce(state: CategoriesState, change: CategoriesChange): CategoriesState {
        return when (change) {
            is CategoriesChange.PageLoaded -> when (change.data) {
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
            is CategoriesChange.PageLoadingChanged -> state.copy(isPageLoading = change.isLoading)
            is CategoriesChange.RefreshChanged -> state.copy(isRefreshing = change.isRefreshing)
            is CategoriesChange.LastVisibleItemChanged -> state.copy(lastVisibleItemId = change.id)
            is CategoriesChange.NetworkChanged -> state.copy(isNetworkConnected = change.isConnected)
        }
    }
}
