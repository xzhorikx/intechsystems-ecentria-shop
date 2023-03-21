package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIModel

data class ProductsModel(
    val items: List<ProductsItem>,
    val category: String,
    val isRefreshing: Boolean
) : UIModel