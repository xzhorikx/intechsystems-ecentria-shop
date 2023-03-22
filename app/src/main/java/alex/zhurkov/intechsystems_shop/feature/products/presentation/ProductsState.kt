package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIState
import alex.zhurkov.intechsystems_shop.common.whenFalse
import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.model.Product

data class ProductsState(
    val categoryId: String,
    val categoryUrl: String,
    val category: Category?,
    val pages: List<Page<Product>>,
    val isPageLoading: Boolean,
    val isRefreshing: Boolean,
    val lastVisibleItemId: Long?,
    val isNetworkConnected: Boolean?
) : UIState {

    companion object {
        fun getInitial(categoryId: String, categoryUrl: String) = ProductsState(
            categoryId = categoryId,
            categoryUrl = categoryUrl,
            pages = emptyList(),
            category = null,
            isPageLoading = false,
            isRefreshing = false,
            lastVisibleItemId = null,
            isNetworkConnected = null
        )
    }

    fun isPageLoaded(pageId: Int): Boolean = pages.any { it.pageId == pageId }

    val isLastPageLoaded = pages.any(Page<Product>::isLastPage)

    val initialPageId = 1

    val nextPage =
        isLastPageLoaded.whenFalse { (pages.lastOrNull()?.pageId?.inc() ?: initialPageId) }

    val lastProductId = pages.flatMap { it.items }.lastOrNull()?.productId
}
