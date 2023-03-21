package alex.zhurkov.intechsystems_shop.feature.product_details.di

import alex.zhurkov.intechsystems_shop.app.di.ActivityScope
import alex.zhurkov.intechsystems_shop.feature.product_details.ProductDetailsActivity
import alex.zhurkov.intechsystems_shop.feature.product_details.model.ProductDetailsInputData
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        ProductDetailsPresentationModule::class
    ]
)
interface ProductDetailsComponent {

    fun inject(target: ProductDetailsActivity)

    interface ComponentProvider {
        fun provideProductDetailsComponent(
            activity: ProductDetailsActivity,
            inputData: ProductDetailsInputData
        ): ProductDetailsComponent
    }

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: ProductDetailsActivity,
            @BindsInstance inputData: ProductDetailsInputData
        ): ProductDetailsComponent
    }
}