package alex.zhurkov.intechsystems_shop.data.database.dao

import alex.zhurkov.intechsystems_shop.data.database.entity.ProductEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query(
        """
        SELECT * FROM product
        WHERE categoryId = :categoryId
        LIMIT :limit OFFSET :offset
        """
    )
    suspend fun getProducts(categoryId: String, limit: Int, offset: Int): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(categories: List<ProductEntity>)
}
