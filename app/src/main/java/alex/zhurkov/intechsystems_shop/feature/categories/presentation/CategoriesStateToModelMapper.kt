package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Page

class CategoriesStateToModelMapper : StateToModelMapper<CategoriesState, CategoriesModel> {
    override fun mapStateToModel(state: CategoriesState): CategoriesModel {
        val items = mapItems(
            pages = state.pages,
            isPageLoading = state.isPageLoading,
        )
        return CategoriesModel(
            items = items,
            isRefreshing = state.isRefreshing,
        )
    }

    private fun mapItems(
        pages: List<Page<Category>>,
        isPageLoading: Boolean,
    ): List<CategoryItem> {
        val items = pages.flatMap { page ->
            page.items.map { category -> category.toItem() }
        }
        val loadingIndicators = when (isPageLoading) {
            true -> (0..7).map { CategoryItem.Loading(id = "loading_$it") }
            false -> emptyList()
        }
        return (items + loadingIndicators).distinctBy { it.id }
    }
}