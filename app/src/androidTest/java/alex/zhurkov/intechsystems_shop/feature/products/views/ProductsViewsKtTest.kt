package alex.zhurkov.intechsystems_shop.feature.products.views


import alex.zhurkov.intechsystems_shop.app.theme.ShopTheme
import alex.zhurkov.intechsystems_shop.common.singleItemList
import alex.zhurkov.intechsystems_shop.feature.products.presentation.ProductsItem
import alex.zhurkov.intechsystems_shop.feature.products.presentation.ProductsModel
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.lifecycle.MutableLiveData
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProductsViewsKtTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val categoryId = "categoryId"
    private fun buildDataItem(id: Long) = ProductsItem.Data(
        id = id,
        categoryId = categoryId,
        url = "url",
        imageUrl = "imageUrl",
        name = "name",
        listPrice = 123.123,
    )

    @Test
    fun when_ui_model_has_data_items_they_are_displayed() {
        val itemCount = 3
        val loadingItems = (0..itemCount).map {
            buildDataItem(id = it.toLong())
        }
        setMainScreenContent(
            ProductsModel(
                items = loadingItems,
                isRefreshing = false,
                category = categoryId
            )
        )
        (0..itemCount).forEach {
            rule.onNodeWithTag("item_$it").assertExists()
        }
    }

    @Test
    fun when_ui_model_has_loading_items_they_are_displayed() {
        val itemCount = 3
        val loadingItems = (0..itemCount).map {
            ProductsItem.Loading(id = it.toLong())
        }
        setMainScreenContent(
            ProductsModel(
                items = loadingItems,
                isRefreshing = false,
                category = categoryId
            )
        )

        (0..itemCount).forEach {
            rule.onNodeWithTag("loading_$it").assertExists()
        }
    }

    @Test
    fun when_data_item_is_clicked_then_onClick_is_called() {
        val id = 9899L
        var onClickInvocations = 0
        setMainScreenContent(
            model = ProductsModel(
                items = buildDataItem(id = id).singleItemList(),
                isRefreshing = false,
                category = categoryId
            ),
            onClick = { onClickInvocations++ }
        )
        rule.onNodeWithTag("item_$id").performClick()
        assertEquals(1, onClickInvocations)
    }

    @Test
    fun when_last_item_is_updated_then_onLastItemVisible_is_called() {
        val itemCount = 10
        val loadingItems = (0..itemCount).map {
            buildDataItem(id = it.toLong())
        }
        var lastItemId: Long? = null
        setMainScreenContent(
            model = ProductsModel(
                items = loadingItems,
                isRefreshing = false,
                category = categoryId
            ),
            onLastItemVisible = { lastItemId = it }
        )
        rule.onNodeWithTag("item_container").performTouchInput { swipeUp() }
        assertEquals(itemCount, lastItemId?.toInt())
    }

    private fun setMainScreenContent(
        model: ProductsModel,
        onBackClick: (() -> Unit)? = null,
        onClick: (() -> Unit)? = null,
        onLastItemVisible: ((id: Long) -> Unit)? = null
    ) {
        rule.setContent {
            ShopTheme {
                ProductsScreen(
                    uiModel = MutableLiveData(model),
                    onPullToRefresh = { },
                    onLastItemVisible = { onLastItemVisible?.invoke(it) },
                    onClick = { onClick?.invoke() },
                    onBackClick = { onBackClick?.invoke() }
                )
            }
        }
    }
}