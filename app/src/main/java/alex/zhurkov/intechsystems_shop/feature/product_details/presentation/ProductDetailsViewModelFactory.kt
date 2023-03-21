package alex.zhurkov.intechsystems_shop.feature.product_details.presentation

import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.domain.repository.ProductDetailsRepository
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import alex.zhurkov.intechsystems_shop.feature.product_details.model.ProductDetailsInputData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductDetailsViewModelFactory(
    private val inputData: ProductDetailsInputData,
    private val productDetailsRepository: ProductDetailsRepository,
    private val networkConnectionUseCase: NetworkConnectionUseCase,
    private val reducer: Reducer<ProductDetailsState, ProductDetailsChange>,
    private val stateToModelMapper: StateToModelMapper<ProductDetailsState, ProductDetailsModel>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProductDetailsViewModel(
        inputData = inputData,
        productDetailsRepository = productDetailsRepository,
        networkConnectionUseCase = networkConnectionUseCase,
        reducer = reducer,
        stateToModelMapper = stateToModelMapper
    ) as T
}
