package alex.zhurkov.intechsystems_shop.feature.products.presentation

import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.domain.repository.CategoriesRepository
import alex.zhurkov.intechsystems_shop.domain.repository.ProductsRepository
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import alex.zhurkov.intechsystems_shop.feature.products.model.ProductsInputData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductsViewModelFactory(
    private val inputData: ProductsInputData,
    private val productsRepository: ProductsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val networkConnectionUseCase: NetworkConnectionUseCase,
    private val reducer: Reducer<ProductsState, ProductsChange>,
    private val stateToModelMapper: StateToModelMapper<ProductsState, ProductsModel>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProductsViewModel(
        inputData = inputData,
        categoriesRepository = categoriesRepository,
        productsRepository = productsRepository,
        networkConnectionUseCase = networkConnectionUseCase,
        reducer = reducer,
        stateToModelMapper = stateToModelMapper
    ) as T
}
