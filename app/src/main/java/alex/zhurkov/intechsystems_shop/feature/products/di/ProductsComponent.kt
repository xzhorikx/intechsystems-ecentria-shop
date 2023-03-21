package alex.zhurkov.intechsystems_shop.feature.products.di

import alex.zhurkov.intechsystems_shop.app.di.ActivityScope
import alex.zhurkov.intechsystems_shop.feature.products.ProductsActivity
import alex.zhurkov.intechsystems_shop.feature.products.model.ProductsInputData
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        ProductsPresentationModule::class
    ]
)
interface ProductsComponent {

    fun inject(target: ProductsActivity)

    interface ComponentProvider {
        fun provideProductsComponent(
            activity: ProductsActivity,
            inputData: ProductsInputData
        ): ProductsComponent
    }

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: ProductsActivity,
            @BindsInstance inputData: ProductsInputData
        ): ProductsComponent
    }
}