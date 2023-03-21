package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.common.arch.UIAction

sealed class CategoriesAction : UIAction {
    object Refresh : CategoriesAction()
    data class LastVisibleItemChanged(val id: String) : CategoriesAction()
}