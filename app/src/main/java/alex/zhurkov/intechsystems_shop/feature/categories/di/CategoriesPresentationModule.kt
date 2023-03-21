package alex.zhurkov.intechsystems_shop.feature.categories.di

import alex.zhurkov.intechsystems_shop.app.di.ActivityScope
import alex.zhurkov.intechsystems_shop.common.arch.Reducer
import alex.zhurkov.intechsystems_shop.common.arch.StateToModelMapper
import alex.zhurkov.intechsystems_shop.domain.repository.CategoriesRepository
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import alex.zhurkov.intechsystems_shop.feature.categories.presentation.*
import dagger.Module
import dagger.Provides

@Module
class CategoriesPresentationModule {

    @Provides
    @ActivityScope
    fun reducer(): Reducer<CategoriesState, CategoriesChange> = CategoriesReducer()

    @Provides
    @ActivityScope
    fun stateToModelMapper(): StateToModelMapper<CategoriesState, CategoriesModel> =
        CategoriesStateToModelMapper()

    @Provides
    @ActivityScope
    fun viewModelFactory(
        categoriesRepository: CategoriesRepository,
        networkConnectionUseCase: NetworkConnectionUseCase,
        reducer: Reducer<CategoriesState, CategoriesChange>,
        stateToModelMapper: StateToModelMapper<CategoriesState, CategoriesModel>
    ) = CategoriesViewModelFactory(
        categoriesRepository = categoriesRepository,
        networkConnectionUseCase = networkConnectionUseCase,
        reducer = reducer,
        stateToModelMapper = stateToModelMapper
    )
}
