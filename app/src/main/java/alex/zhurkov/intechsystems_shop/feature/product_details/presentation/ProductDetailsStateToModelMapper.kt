package alex.zhurkov.intechsystems_shop.feature.product_details.presentation

import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.domain.model.ProductDetails

private const val IMAGE_URL_BASE = "https://op1.0ps.us/365-240-ffffff/"
private const val IMAGE_FORMAT = "jpg"

class ProductDetailsStateToModelMapper :
    StateToModelMapper<ProductDetailsState, ProductDetailsModel> {
    override fun mapStateToModel(state: ProductDetailsState): ProductDetailsModel {
        val item = mapItem(
            productDetails = state.productDetails
        )
        return ProductDetailsModel(item = item)
    }

    private fun mapItem(
        productDetails: ProductDetails?,
    ): ProductDetailsItem = when (productDetails) {
        null -> ProductDetailsItem.Loading()
        else -> productDetails.toItem(imageUrlBase = IMAGE_URL_BASE, imageFormat = IMAGE_FORMAT)
    }
}