package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIState
import alex.zhurkov.intechsystems_shop.common.whenFalse
import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page

data class CategoriesState(
    val pages: List<Page<Category>>,
    val isPageLoading: Boolean,
    val isRefreshing: Boolean,
    val lastVisibleItemId: String?,
    val isNetworkConnected: Boolean?
) : UIState {

    companion object {
        val EMPTY = CategoriesState(
            pages = emptyList(),
            isPageLoading = false,
            isRefreshing = false,
            lastVisibleItemId = null,
            isNetworkConnected = null
        )
    }

    fun isPageLoaded(pageId: Int): Boolean = pages.any { it.pageId == pageId }

    val isLastPageLoaded = pages.any(Page<Category>::isLastPage)

    val initialPageId = 1

    val nextPage =
        isLastPageLoaded.whenFalse { (pages.lastOrNull()?.pageId?.inc() ?: initialPageId) }

    val lastRepoId = pages.flatMap { it.items }.lastOrNull()?.categoryId

}
