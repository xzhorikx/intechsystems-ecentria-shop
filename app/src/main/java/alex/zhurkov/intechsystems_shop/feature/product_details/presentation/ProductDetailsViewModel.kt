package alex.zhurkov.intechsystems_shop.feature.product_details.presentation

import alex.zhurkov.intechsystems_shop.common.arch.BaseViewModel
import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.common.whenTrue
import alex.zhurkov.intechsystems_shop.domain.repository.ProductDetailsRepository
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import alex.zhurkov.intechsystems_shop.feature.product_details.model.ProductDetailsInputData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class ProductDetailsViewModel(
    private val inputData: ProductDetailsInputData,
    private val productDetailsRepository: ProductDetailsRepository,
    private val networkConnectionUseCase: NetworkConnectionUseCase,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    reducer: Reducer<ProductDetailsState, ProductDetailsChange>,
    stateToModelMapper: StateToModelMapper<ProductDetailsState, ProductDetailsModel>
) : BaseViewModel<ProductDetailsAction, ProductDetailsChange, ProductDetailsState, ProductDetailsModel>(
    dispatcher = dispatcher, reducer = reducer, stateToModelMapper = stateToModelMapper
) {
    override var state = ProductDetailsState.getInitial(productId = inputData.productId)

    override fun onStateUpdated(oldState: ProductDetailsState, newState: ProductDetailsState) {
        super.onStateUpdated(oldState, newState)
        val isNetworkChanged =
            oldState.isNetworkConnected != null && (oldState.isNetworkConnected != newState.isNetworkConnected)

        isNetworkChanged.whenTrue {
            newState.isNetworkConnected?.let {
                sendEvent(ProductDetailsEvent.NetworkConnectionChanged(it))
            }
        }
    }

    override suspend fun provideChangesObservable(): Flow<ProductDetailsChange> = merge(
        productDetailsRepository.observeDetails(productId = inputData.productId)
            .map { ProductDetailsChange.ProductDetailsChanged(data = it) },
        networkConnectionUseCase.observeConnectionState()
            .map { ProductDetailsChange.NetworkChanged(isConnected = it) })

    override fun processAction(action: ProductDetailsAction) {
        /* TODO handle changes if any */
    }
}
