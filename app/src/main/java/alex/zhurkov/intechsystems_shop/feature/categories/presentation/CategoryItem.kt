package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.domain.model.Category
import java.util.*

sealed class CategoryItem {
    abstract val id: String

    data class Data(
        override val id: String,
        val fullName: String,
        val url: String
    ) : CategoryItem()

    data class Loading(override val id: String = UUID.randomUUID().toString()) : CategoryItem()
}

fun Category.toItem() = CategoryItem.Data(id = categoryId, fullName = fullName, url = url)