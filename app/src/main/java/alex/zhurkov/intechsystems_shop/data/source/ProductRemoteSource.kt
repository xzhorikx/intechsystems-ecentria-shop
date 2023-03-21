package alex.zhurkov.intechsystems_shop.data.source

import retrofit2.http.GET
import retrofit2.http.Path

interface ProductRemoteSource {
    @GET("catalog/{category}/products?_iv_include=gridProducts")
    suspend fun getProducts(@Path("category") category: String): ProductsResponse
}