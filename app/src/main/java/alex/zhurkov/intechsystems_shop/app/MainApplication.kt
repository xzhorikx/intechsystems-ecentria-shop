package alex.zhurkov.intechsystems_shop.app

import alex.zhurkov.intechsystems_shop.app.di.DaggerAppComponent
import alex.zhurkov.intechsystems_shop.feature.categories.CategoriesActivity
import alex.zhurkov.intechsystems_shop.feature.categories.di.CategoriesComponent
import alex.zhurkov.intechsystems_shop.feature.product_details.ProductDetailsActivity
import alex.zhurkov.intechsystems_shop.feature.product_details.di.ProductDetailsComponent
import alex.zhurkov.intechsystems_shop.feature.product_details.model.ProductDetailsInputData
import alex.zhurkov.intechsystems_shop.feature.products.ProductsActivity
import alex.zhurkov.intechsystems_shop.feature.products.di.ProductsComponent
import alex.zhurkov.intechsystems_shop.feature.products.model.ProductsInputData
import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import timber.log.Timber
import javax.inject.Inject

class MainApplication : Application(), CategoriesComponent.ComponentProvider,
    ProductsComponent.ComponentProvider, ProductDetailsComponent.ComponentProvider,
    ImageLoaderFactory {
    private val component by lazy { DaggerAppComponent.factory().create(this) }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        component.inject(this)
        Timber.plant(Timber.DebugTree())
        super.onCreate()
    }

    override fun newImageLoader(): ImageLoader = imageLoader

    override fun provideCategoriesComponent(activity: CategoriesActivity): CategoriesComponent =
        component.plusCategoriesComponent().create(activity)

    override fun provideProductsComponent(
        activity: ProductsActivity,
        inputData: ProductsInputData
    ): ProductsComponent =
        component.plusProductsComponent().create(activity = activity, inputData = inputData)

    override fun provideProductDetailsComponent(
        activity: ProductDetailsActivity,
        inputData: ProductDetailsInputData
    ): ProductDetailsComponent =
        component.plusProductDetailsComponent().create(activity = activity, inputData = inputData)
}