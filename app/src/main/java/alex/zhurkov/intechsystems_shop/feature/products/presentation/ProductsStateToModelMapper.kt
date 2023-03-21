package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.domain.model.Page
import alex.zhurkov.intechsystems_shop.domain.model.Product

private const val IMAGE_URL_BASE = "https://op1.0ps.us/120-90-ffffff/"
private const val IMAGE_FORMAT = "jpg"

class ProductsStateToModelMapper : StateToModelMapper<ProductsState, ProductsModel> {
    override fun mapStateToModel(state: ProductsState): ProductsModel {
        val items = mapItems(
            pages = state.pages,
            isPageLoading = state.isPageLoading,
        )
        return ProductsModel(
            items = items,
            isRefreshing = state.isRefreshing,
            category = state.category?.fullName.orEmpty()
        )
    }

    private fun mapItems(
        pages: List<Page<Product>>,
        isPageLoading: Boolean,
    ): List<ProductsItem> {
        val items = pages.flatMap { page ->
            page.items.map { product ->
                product.toItem(
                    imageUrlBase = IMAGE_URL_BASE, imageFormat = IMAGE_FORMAT
                )
            }
        }
        val loadingIndicators = when (isPageLoading) {
            true -> (0..7).map { ProductsItem.Loading(id = it.toLong()) }
            false -> emptyList()
        }
        return (items + loadingIndicators).distinctBy { it.id }
    }
}