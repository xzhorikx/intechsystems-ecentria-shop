package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.common.arch.BaseViewModel
import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.common.whenTrue
import alex.zhurkov.intechsystems_shop.domain.repository.CategoriesRepository
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.CancellationException

class CategoriesViewModel(
    private val categoriesRepository: CategoriesRepository,
    private val networkConnectionUseCase: NetworkConnectionUseCase,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    reducer: Reducer<CategoriesState, CategoriesChange>,
    stateToModelMapper: StateToModelMapper<CategoriesState, CategoriesModel>
) : BaseViewModel<CategoriesAction, CategoriesChange, CategoriesState, CategoriesModel>(
    dispatcher = dispatcher, reducer = reducer, stateToModelMapper = stateToModelMapper
) {
    override var state = CategoriesState.EMPTY
    private var pageJob: Job? = null
    override fun onObserverActive(isFirstTime: Boolean) {
        super.onObserverActive(isFirstTime)
        if (isFirstTime) {
            state.nextPage?.let { loadCategoriesPage(pageIndex = it) }
        }
    }

    override fun onStateUpdated(oldState: CategoriesState, newState: CategoriesState) {
        super.onStateUpdated(oldState, newState)
        val isLastVisibleItemUpdated = oldState.lastVisibleItemId != newState.lastVisibleItemId
        val shouldLoadNextPage =
            isLastVisibleItemUpdated && newState.lastRepoId == newState.lastVisibleItemId
        val isNetworkChanged =
            oldState.isNetworkConnected != null && (oldState.isNetworkConnected != newState.isNetworkConnected)

        if (shouldLoadNextPage) {
            state.nextPage?.let { loadCategoriesPage(pageIndex = it) }
        }
        isNetworkChanged.whenTrue {
            newState.isNetworkConnected?.let {
                sendEvent(CategoriesEvent.NetworkConnectionChanged(it))
            }
        }
    }

    override suspend fun provideChangesObservable(): Flow<CategoriesChange> =
        networkConnectionUseCase.observeConnectionState()
            .map { CategoriesChange.NetworkChanged(isConnected = it) }

    override fun processAction(action: CategoriesAction) {
        when (action) {
            CategoriesAction.Refresh -> refreshCategories()
            is CategoriesAction.LastVisibleItemChanged -> {
                if (action.id != state.lastVisibleItemId) {
                    sendChange(CategoriesChange.LastVisibleItemChanged(action.id))
                }
                Unit
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        pageJob?.cancel()
    }

    private fun refreshCategories() {
        loadCategoriesPage(
            pageIndex = state.initialPageId,
            forceRefresh = true
        )
    }

    private fun loadCategoriesPage(
        pageIndex: Int,
        forceRefresh: Boolean = false
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
            execute(
                action = {
                    categoriesRepository.getCategoryPage(page = pageIndex, skipCache = forceRefresh)
                },
                onStart = {
                    sendChange(CategoriesChange.PageLoadingChanged(isLoading = true))
                    forceRefresh.whenTrue {
                        sendChange(CategoriesChange.RefreshChanged(isRefreshing = true))
                    }
                },
                onSuccess = { sendChange(CategoriesChange.PageLoaded(it)) },
                onComplete = {
                    sendChange(CategoriesChange.PageLoadingChanged(isLoading = false))
                    forceRefresh.whenTrue {
                        sendChange(CategoriesChange.RefreshChanged(isRefreshing = false))
                    }

                },
                onErrorOccurred = ::onPageLoadingError
            )
        }
    }

    private fun onPageLoadingError(e: Throwable) {
        sendEvent(CategoriesEvent.DisplayError(e))
        Timber.e(e)
    }
}
