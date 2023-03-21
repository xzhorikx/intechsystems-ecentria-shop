package alex.zhurkov.intechsystems_shop.app.di

import alex.zhurkov.intechsystems_shop.data.network.interceptor.AuthControlInterceptor
import alex.zhurkov.intechsystems_shop.data.network.interceptor.CacheControlInterceptor
import alex.zhurkov.intechsystems_shop.data.source.CategoryRemoteSource
import alex.zhurkov.intechsystems_shop.data.source.ProductDetailsRemoteSource
import alex.zhurkov.intechsystems_shop.data.source.ProductRemoteSource
import alex.zhurkov.intechsystems_shop.domain.config.ConfigSource
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Reusable
    fun clientBuilder(
        @AppContext context: Context,
        configSource: ConfigSource
    ): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, configSource.cacheSizeByte))
            .callTimeout(configSource.callTimeOutSec, TimeUnit.SECONDS)
            .connectTimeout(configSource.callTimeOutSec, TimeUnit.SECONDS)
            .readTimeout(configSource.callTimeOutSec, TimeUnit.SECONDS)
            .writeTimeout(configSource.callTimeOutSec, TimeUnit.SECONDS)

    @Provides
    @Singleton
    @LoggingInterceptor
    fun loggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    @CacheInterceptor
    fun cacheInterceptor(configSource: ConfigSource): Interceptor =
        CacheControlInterceptor(configSource)

    @Provides
    @Singleton
    @AuthInterceptor
    fun authInterceptor(configSource: ConfigSource): Interceptor =
        AuthControlInterceptor(configSource)

    @Provides
    @Singleton
    @ShopHttpClient
    fun shopHttpClient(
        builder: OkHttpClient.Builder,
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @AuthInterceptor authInterceptor: Interceptor,
    ): OkHttpClient = builder
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    @Provides
    @Singleton
    @ImageLoaderHttpClient
    fun imageLoaderHttpClient(
        builder: OkHttpClient.Builder,
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @CacheInterceptor cacheInterceptor: Interceptor
    ): OkHttpClient = builder
        .addInterceptor(cacheInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun categoryRemoteSource(
        @ShopHttpClient httpClient: OkHttpClient
    ): CategoryRemoteSource = Retrofit.Builder()
        .baseUrl("https://www.opticsplanet.com/api/0.2/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(CategoryRemoteSource::class.java)

    @Provides
    @Singleton
    fun productsRemoteSource(
        @ShopHttpClient httpClient: OkHttpClient
    ): ProductRemoteSource = Retrofit.Builder()
        .baseUrl("https://www.opticsplanet.com/iv-api/0.3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(ProductRemoteSource::class.java)

    @Provides
    @Singleton
    fun productDetailsRemoteSource(
        @ShopHttpClient httpClient: OkHttpClient
    ): ProductDetailsRemoteSource = Retrofit.Builder()
        .baseUrl("https://www.opticsplanet.com/api/0.3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(ProductDetailsRemoteSource::class.java)
}
