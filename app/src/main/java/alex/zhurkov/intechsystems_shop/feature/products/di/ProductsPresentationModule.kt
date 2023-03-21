package alex.zhurkov.intechsystems_shop.feature.products.di

import alex.zhurkov.intechsystems_shop.app.di.ActivityScope
import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.domain.repository.CategoriesRepository
import alex.zhurkov.intechsystems_shop.domain.repository.ProductsRepository
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import alex.zhurkov.intechsystems_shop.feature.products.model.ProductsInputData
import alex.zhurkov.intechsystems_shop.feature.products.presentation.*
import dagger.Module
import dagger.Provides

@Module
class ProductsPresentationModule {

    @Provides
    @ActivityScope
    fun reducer(): Reducer<ProductsState, ProductsChange> = ProductsReducer()

    @Provides
    @ActivityScope
    fun stateToModelMapper(): StateToModelMapper<ProductsState, ProductsModel> =
        ProductsStateToModelMapper()

    @Provides
    @ActivityScope
    fun viewModelFactory(
        inputData: ProductsInputData,
        productsRepository: ProductsRepository,
        categoriesRepository: CategoriesRepository,
        networkConnectionUseCase: NetworkConnectionUseCase,
        reducer: Reducer<ProductsState, ProductsChange>,
        stateToModelMapper: StateToModelMapper<ProductsState, ProductsModel>
    ) = ProductsViewModelFactory(
        inputData = inputData,
        productsRepository = productsRepository,
        categoriesRepository = categoriesRepository,
        networkConnectionUseCase = networkConnectionUseCase,
        reducer = reducer,
        stateToModelMapper = stateToModelMapper
    )
}
