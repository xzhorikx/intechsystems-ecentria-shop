package alex.zhurkov.intechsystems_shop.data.source

import retrofit2.http.GET

interface CategoryRemoteSource {
    @GET("categories")
    suspend fun getCategories(): List<CategoryResponse>
}
