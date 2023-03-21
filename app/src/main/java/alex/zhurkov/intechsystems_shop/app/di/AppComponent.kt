package alex.zhurkov.intechsystems_shop.app.di

import alex.zhurkov.intechsystems_shop.app.MainApplication
import alex.zhurkov.intechsystems_shop.feature.categories.di.CategoriesComponent
import alex.zhurkov.intechsystems_shop.feature.products.di.ProductsComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        ImageLoaderModule::class,
        DataModule::class,
        NetworkConnectionModule::class,
    ]
)
interface AppComponent {
    fun inject(target: MainApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: MainApplication): AppComponent
    }

    fun plusCategoriesComponent(): CategoriesComponent.Factory
    fun plusProductsComponent(): ProductsComponent.Factory
}