@file:OptIn(ExperimentalTextApi::class)

package alex.zhurkov.intechsystems_shop.feature.categories.views

import alex.zhurkov.intechsystems_shop.R
import alex.zhurkov.intechsystems_shop.app.theme.ShopTheme
import alex.zhurkov.intechsystems_shop.feature.categories.presentation.CategoriesModel
import alex.zhurkov.intechsystems_shop.feature.categories.presentation.CategoryItem
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.LiveData
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiModel: LiveData<CategoriesModel>,
    onPullToRefresh: () -> Unit,
    onLastItemVisible: (id: String) -> Unit,
    onClick: (CategoryItem.Data) -> Unit,
) {
    val model by uiModel.observeAsState()
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    model?.let { renderModel ->
        ShopTheme {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                stringResource(id = R.string.categories)
                            )
                        },
                        scrollBehavior = scrollBehavior,
                    )
                }
            ) { paddingValues ->
                CategoriesContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            PaddingValues(
                                start = dimensionResource(id = R.dimen.padding_16),
                                end = dimensionResource(id = R.dimen.padding_16),
                                top = paddingValues.calculateTopPadding(),
                                bottom = paddingValues.calculateBottomPadding()
                            )
                        ),
                    renderModel = renderModel,
                    onPullToRefresh = onPullToRefresh,
                    onLastItemVisible = onLastItemVisible,
                    onClick = onClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
fun CategoriesContent(
    modifier: Modifier = Modifier,
    renderModel: CategoriesModel,
    onPullToRefresh: () -> Unit,
    onLastItemVisible: (id: String) -> Unit,
    onClick: (CategoryItem.Data) -> Unit,
) {
    // The LaunchedEffect is needed to fix the hiding of the indicator: https://issuetracker.google.com/issues/248274004
    // Without this workaround the indicator would be visible even if the loading finished
    var isRefreshingWorkaround by remember { mutableStateOf(renderModel.isRefreshing) }
    LaunchedEffect(key1 = renderModel.isRefreshing) {
        isRefreshingWorkaround = renderModel.isRefreshing
    }
    val pullToRefreshState = rememberPullRefreshState(isRefreshingWorkaround, onPullToRefresh)
    val state = rememberLazyListState()
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box(Modifier.pullRefresh(pullToRefreshState)) {
            LazyColumn(
                modifier = modifier.testTag("item_container"),
                state = state,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_8)),
                contentPadding = PaddingValues(
                    vertical = dimensionResource(id = R.dimen.padding_8)
                )
            ) {
                itemsIndexed(
                    items = renderModel.items,
                    key = { _, item -> item.id }) { index, item ->
                    val lastVisibleId by remember {
                        derivedStateOf {
                            with(state.layoutInfo) {
                                (visibleItemsInfo.lastOrNull()?.key as? String)
                                    .takeIf { it == (item as? CategoryItem.Data)?.id }
                            }
                        }
                    }
                    lastVisibleId?.run(onLastItemVisible)
                    when (item) {
                        is CategoryItem.Data -> {
                            CategoryPreview(
                                item,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onClick,
                            )
                        }
                        is CategoryItem.Loading -> {
                            CategoryLoading(
                                item = item,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = R.dimen.shimmer_height))
                            )
                        }
                    }
                    if (index < renderModel.items.lastIndex) {
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_8)))
                        Divider(
                            color = MaterialTheme.colorScheme.outline,
                            thickness = dimensionResource(id = R.dimen.divider_thickness)
                        )
                    }
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = renderModel.isRefreshing,
                state = pullToRefreshState,
            )
        }
    }
}

@Composable
fun CategoryPreview(
    item: CategoryItem.Data,
    modifier: Modifier = Modifier,
    onClick: (CategoryItem.Data) -> Unit,
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clickable { onClick(item) }) {
        Text(item.fullName)
    }
}

@Composable
fun CategoryLoading(
    modifier: Modifier = Modifier,
    item: CategoryItem.Loading
) {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.Window,
        theme = LocalShimmerTheme.current.copy(
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 600,
                    easing = LinearEasing,
                    delayMillis = 300,
                ),
                repeatMode = RepeatMode.Restart,
            ),
        ),
    )
    Box(
        modifier = modifier
            .shimmer(shimmer)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .testTag(item.id)
    )
}
