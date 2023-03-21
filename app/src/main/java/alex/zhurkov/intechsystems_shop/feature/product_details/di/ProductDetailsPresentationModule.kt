package alex.zhurkov.intechsystems_shop.feature.product_details.di

import alex.zhurkov.intechsystems_shop.app.di.ActivityScope
import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.domain.repository.ProductDetailsRepository
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import alex.zhurkov.intechsystems_shop.feature.product_details.model.ProductDetailsInputData
import alex.zhurkov.intechsystems_shop.feature.product_details.presentation.*
import dagger.Module
import dagger.Provides

@Module
class ProductDetailsPresentationModule {

    @Provides
    @ActivityScope
    fun reducer(): Reducer<ProductDetailsState, ProductDetailsChange> = ProductDetailsReducer()

    @Provides
    @ActivityScope
    fun stateToModelMapper(): StateToModelMapper<ProductDetailsState, ProductDetailsModel> =
        ProductDetailsStateToModelMapper()

    @Provides
    @ActivityScope
    fun viewModelFactory(
        inputData: ProductDetailsInputData,
        productDetailsRepository: ProductDetailsRepository,
        networkConnectionUseCase: NetworkConnectionUseCase,
        reducer: Reducer<ProductDetailsState, ProductDetailsChange>,
        stateToModelMapper: StateToModelMapper<ProductDetailsState, ProductDetailsModel>
    ) = ProductDetailsViewModelFactory(
        inputData = inputData,
        productDetailsRepository = productDetailsRepository,
        networkConnectionUseCase = networkConnectionUseCase,
        reducer = reducer,
        stateToModelMapper = stateToModelMapper
    )
}
