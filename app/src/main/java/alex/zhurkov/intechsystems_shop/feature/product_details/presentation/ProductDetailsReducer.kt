package alex.zhurkov.intechsystems_shop.feature.product_details.presentation

import alex.zhurkov.intechsystems_shop.common.arch.Reducer

class ProductDetailsReducer : Reducer<ProductDetailsState, ProductDetailsChange> {
    override fun reduce(
        state: ProductDetailsState,
        change: ProductDetailsChange
    ): ProductDetailsState {
        return when (change) {
            is ProductDetailsChange.ProductDetailsChanged -> state.copy(productDetails = change.data)
            is ProductDetailsChange.NetworkChanged -> state.copy(isNetworkConnected = change.isConnected)
        }
    }
}
