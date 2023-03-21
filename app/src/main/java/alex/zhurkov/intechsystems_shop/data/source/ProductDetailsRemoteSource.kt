package alex.zhurkov.intechsystems_shop.data.source

import retrofit2.http.GET
import retrofit2.http.Path

interface ProductDetailsRemoteSource {
    @GET("products/{productId}")
    suspend fun getProductDetails(@Path("productId") productId: Long): ProductDetailsResponse
}
