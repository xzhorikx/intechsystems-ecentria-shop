package alex.zhurkov.intechsystems_shop.feature.categories.presentation

import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.domain.repository.CategoriesRepository
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CategoriesViewModelFactory(
    private val categoriesRepository: CategoriesRepository,
    private val networkConnectionUseCase: NetworkConnectionUseCase,
    private val reducer: Reducer<CategoriesState, CategoriesChange>,
    private val stateToModelMapper: StateToModelMapper<CategoriesState, CategoriesModel>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CategoriesViewModel(
        categoriesRepository = categoriesRepository,
        networkConnectionUseCase = networkConnectionUseCase,
        reducer = reducer,
        stateToModelMapper = stateToModelMapper
    ) as T
}
