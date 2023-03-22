package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.BaseViewModel
import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.common.whenTrue
import alex.zhurkov.intechsystems_shop.domain.repository.CategoriesRepository
import alex.zhurkov.intechsystems_shop.domain.repository.ProductsRepository
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import alex.zhurkov.intechsystems_shop.feature.products.model.ProductsInputData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.CancellationException

class ProductsViewModel(
    inputData: ProductsInputData,
    private val productsRepository: ProductsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val networkConnectionUseCase: NetworkConnectionUseCase,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    reducer: Reducer<ProductsState, ProductsChange>,
    stateToModelMapper: StateToModelMapper<ProductsState, ProductsModel>
) : BaseViewModel<ProductsAction, ProductsChange, ProductsState, ProductsModel>(
    dispatcher = dispatcher, reducer = reducer, stateToModelMapper = stateToModelMapper
) {
    override var state =
        ProductsState.getInitial(categoryId = inputData.categoryId, categoryUrl = inputData.url)
    private var pageJob: Job? = null
    override fun onObserverActive(isFirstTime: Boolean) {
        super.onObserverActive(isFirstTime)
        if (isFirstTime) {
            state.nextPage?.let { loadProductsPage(pageIndex = it) }
        }
    }

    override fun onStateUpdated(oldState: ProductsState, newState: ProductsState) {
        super.onStateUpdated(oldState, newState)
        val isLastVisibleItemUpdated = oldState.lastVisibleItemId != newState.lastVisibleItemId
        val shouldLoadNextPage =
            isLastVisibleItemUpdated && newState.lastProductId == newState.lastVisibleItemId
        val isNetworkChanged =
            oldState.isNetworkConnected != null && (oldState.isNetworkConnected != newState.isNetworkConnected)

        if (shouldLoadNextPage) {
            state.nextPage?.let { loadProductsPage(pageIndex = it) }
        }
        isNetworkChanged.whenTrue {
            newState.isNetworkConnected?.let {
                sendEvent(ProductsEvent.NetworkConnectionChanged(it))
            }
        }
    }

    override suspend fun provideChangesObservable(): Flow<ProductsChange> = merge(
        categoriesRepository.observeCategory(state.categoryId)
            .map { ProductsChange.CategoryChanged(data = it) },
        networkConnectionUseCase.observeConnectionState()
            .map { ProductsChange.NetworkChanged(isConnected = it) })

    override fun processAction(action: ProductsAction) {
        when (action) {
            ProductsAction.Refresh -> refreshProducts()
            is ProductsAction.LastVisibleItemChanged -> {
                if (action.id != state.lastVisibleItemId) {
                    sendChange(ProductsChange.LastVisibleItemChanged(action.id))
                }
                Unit
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        pageJob?.cancel()
    }

    private fun refreshProducts() {
        loadProductsPage(
            pageIndex = state.initialPageId, forceRefresh = true
        )
    }

    private fun loadProductsPage(
        pageIndex: Int, forceRefresh: Boolean = false
    ) {
        when (forceRefresh) {
            true -> pageJob?.cancel(CancellationException("Force refresh"))
            false -> {
                if (state.isLastPageLoaded) return
                if (state.isPageLoading) return
                if (state.isPageLoaded(pageIndex)) return
            }
        }
        pageJob = viewModelScope.launch(dispatcher) {
            execute(action = {
                productsRepository.getProductsPage(
                    categoryUrl = state.categoryUrl,
                    categoryId = state.categoryId,
                    page = pageIndex,
                    skipCache = forceRefresh
                )
            }, onStart = {
                sendChange(ProductsChange.PageLoadingChanged(isLoading = true))
                forceRefresh.whenTrue {
                    sendChange(ProductsChange.RefreshChanged(isRefreshing = true))
                }
            }, onSuccess = { sendChange(ProductsChange.PageLoaded(it)) }, onComplete = {
                sendChange(ProductsChange.PageLoadingChanged(isLoading = false))
                forceRefresh.whenTrue {
                    sendChange(ProductsChange.RefreshChanged(isRefreshing = false))
                }

            }, onErrorOccurred = ::onPageLoadingError
            )
        }
    }

    private fun onPageLoadingError(e: Throwable) {
        sendEvent(ProductsEvent.DisplayError(e))
        Timber.e(e)
    }
}
