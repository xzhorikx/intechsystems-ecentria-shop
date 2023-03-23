@file:OptIn(ExperimentalTextApi::class)

package alex.zhurkov.intechsystems_shop.feature.products.views

import alex.zhurkov.intechsystems_shop.R
import alex.zhurkov.intechsystems_shop.app.theme.ShopTheme
import alex.zhurkov.intechsystems_shop.feature.products.presentation.ProductsItem
import alex.zhurkov.intechsystems_shop.feature.products.presentation.ProductsModel
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.LiveData
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    uiModel: LiveData<ProductsModel>,
    onPullToRefresh: () -> Unit,
    onLastItemVisible: (id: Long) -> Unit,
    onClick: (ProductsItem.Data) -> Unit,
    onBackClick: () -> Unit,
) {
    val model by uiModel.observeAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    model?.let { renderModel ->
        ShopTheme {
            Scaffold(modifier = modifier.fillMaxSize(), topBar = {
                TopAppBar(title = { Text(renderModel.category) },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back arrow"
                            )
                        }
                    })
            }) { paddingValues ->
                ProductsContent(
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
fun ProductsContent(
    modifier: Modifier = Modifier,
    renderModel: ProductsModel,
    onPullToRefresh: () -> Unit,
    onLastItemVisible: (id: Long) -> Unit,
    onClick: (ProductsItem.Data) -> Unit,
) {
    val pullToRefreshState = rememberPullRefreshState(renderModel.isRefreshing, onPullToRefresh)
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
                itemsIndexed(items = renderModel.items,
                    key = { _, item -> item.id }) { index, item ->
                    val lastVisibleId by remember {
                        derivedStateOf {
                            with(state.layoutInfo) {
                                (visibleItemsInfo.lastOrNull()?.key as? Long).takeIf { it == (item as? ProductsItem.Data)?.id }
                            }
                        }
                    }
                    lastVisibleId?.run(onLastItemVisible)
                    when (item) {
                        is ProductsItem.Data -> {
                            ProductPreview(
                                item,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onClick,
                            )
                        }
                        is ProductsItem.Loading -> {
                            ProductLoading(
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
fun ProductPreview(
    item: ProductsItem.Data,
    modifier: Modifier = Modifier,
    onClick: (ProductsItem.Data) -> Unit,
) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(data = item.imageUrl).crossfade(true).build(),
        error = ColorPainter(Color.Black),
    )
    Row(modifier = modifier
        .testTag("item_${item.id}")
        .fillMaxWidth()
        .clickable { onClick(item) }) {
        Image(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.product_size))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corners_4))),
            painter = painter,
            contentDescription = "Product image",
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_16)))
        Column {
            Text(item.name, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_4)))
            Text(stringResource(id = R.string.price_template, item.listPrice.toString()), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ProductLoading(
    modifier: Modifier = Modifier, item: ProductsItem.Loading
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
            .testTag("loading_${item.id}")
    )
}
