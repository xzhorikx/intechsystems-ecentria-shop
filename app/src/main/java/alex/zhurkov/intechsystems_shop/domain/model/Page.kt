package alex.zhurkov.intechsystems_shop.domain.model

data class Page<T>(
    val items: List<T>,
    val pageId: Int,
    val isLastPage: Boolean,
)