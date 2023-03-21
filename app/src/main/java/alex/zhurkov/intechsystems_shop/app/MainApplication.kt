package alex.zhurkov.intechsystems_shop.app

import alex.zhurkov.intechsystems_shop.app.di.DaggerAppComponent
import alex.zhurkov.intechsystems_shop.feature.categories.CategoriesActivity
import alex.zhurkov.intechsystems_shop.feature.categories.di.CategoriesComponent
import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import timber.log.Timber
import javax.inject.Inject

class MainApplication : Application(), CategoriesComponent.ComponentProvider, ImageLoaderFactory {

    private val component by lazy { DaggerAppComponent.factory().create(this) }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        component.inject(this)
        Timber.plant(Timber.DebugTree())
        super.onCreate()
    }

    override fun newImageLoader(): ImageLoader = imageLoader

    override fun provideMainComponent(activity: CategoriesActivity): CategoriesComponent =
        component.plusHomeActivityComponent().create(activity)
}