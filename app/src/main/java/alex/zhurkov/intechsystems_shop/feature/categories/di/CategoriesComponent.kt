package alex.zhurkov.intechsystems_shop.feature.categories.di

import alex.zhurkov.intechsystems_shop.app.di.ActivityScope
import alex.zhurkov.intechsystems_shop.feature.categories.CategoriesActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        CategoriesPresentationModule::class
    ]
)
interface CategoriesComponent {

    fun inject(target: CategoriesActivity)

    interface ComponentProvider {
        fun provideMainComponent(activity: CategoriesActivity): CategoriesComponent
    }

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: CategoriesActivity): CategoriesComponent
    }
}