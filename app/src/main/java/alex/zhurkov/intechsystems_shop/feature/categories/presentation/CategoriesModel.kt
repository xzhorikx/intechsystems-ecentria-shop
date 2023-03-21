package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIModel

data class CategoriesModel(
    val items: List<CategoryItem>,
    val isRefreshing: Boolean
) : UIModel