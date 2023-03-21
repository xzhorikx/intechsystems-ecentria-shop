package alex.zhurkov.intechsystems_shop.data.network.interceptor

import alex.zhurkov.intechsystems_shop.domain.config.ConfigSource
import okhttp3.Interceptor
import okhttp3.Response

class AuthControlInterceptor(
    private val configSource: ConfigSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val modifiedHeader = original
            .newBuilder()
            .header("x-api-key", configSource.authKey)
            .build()
        return chain.proceed(modifiedHeader)
    }
}