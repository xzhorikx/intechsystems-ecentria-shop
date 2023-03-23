@file:OptIn(ExperimentalTextApi::class)

package alex.zhurkov.intechsystems_shop.feature.product_details.views

import alex.zhurkov.intechsystems_shop.R
import alex.zhurkov.intechsystems_shop.app.theme.ShopTheme
import alex.zhurkov.intechsystems_shop.feature.product_details.presentation.ProductDetailsItem
import alex.zhurkov.intechsystems_shop.feature.product_details.presentation.ProductDetailsModel
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier,
    uiModel: LiveData<ProductDetailsModel>,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
) {
    val model by uiModel.observeAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    model?.let { renderModel ->
        ShopTheme {
            Scaffold(modifier = modifier.fillMaxSize(), topBar = {
                TopAppBar(title = { Text(stringResource(id = R.string.details)) },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back arrow"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { onHomeClick() }) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        }
                    }
                )
            }) { paddingValues ->
                ProductDetailsContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            PaddingValues(
                                start = dimensionResource(id = R.dimen.padding_8),
                                end = dimensionResource(id = R.dimen.padding_8),
                                top = paddingValues.calculateTopPadding(),
                                bottom = paddingValues.calculateBottomPadding()
                            )
                        ),
                    renderModel = renderModel
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
fun ProductDetailsContent(
    modifier: Modifier = Modifier,
    renderModel: ProductDetailsModel
) {
    when (val item = renderModel.item) {
        is ProductDetailsItem.Data -> {
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                Column(
                    modifier = modifier.verticalScroll(rememberScrollState())
                ) {
                    ProductDetails(
                        item,
                        modifier = modifier.fillMaxWidth(),
                    )
                }
            }
        }
        is ProductDetailsItem.Loading -> {
            ProductDetailsLoading(
                item = item,
                modifier = modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.shimmer_height))
            )
        }
    }
}

@Composable
fun ProductDetails(
    item: ProductDetailsItem.Data,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(data = item.imageUrl).crossfade(true).build(),
        error = ColorPainter(Color.Black),
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.product_full_size))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corners_4))),
            painter = painter,
            contentDescription = "Product full image",
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_16)))
        Text(item.name, style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_4)))
        Divider(
            color = MaterialTheme.colorScheme.outline,
            thickness = dimensionResource(id = R.dimen.divider_thickness)
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_4)))
        Text(
            stringResource(id = R.string.price_template, item.price.toString()),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_4)))
        Text(
            stringResource(id = R.string.description_template, item.description),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ProductDetailsLoading(modifier: Modifier = Modifier, item: ProductDetailsItem.Loading) {
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
            .testTag(item.id.toString())
    )
}