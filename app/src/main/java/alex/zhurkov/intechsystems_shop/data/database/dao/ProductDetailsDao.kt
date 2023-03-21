package alex.zhurkov.intechsystems_shop.data.database.dao

import alex.zhurkov.intechsystems_shop.data.database.entity.ProductDetailsEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDetailsDao {
    @Query(
        """
        SELECT * FROM product_details
        WHERE productId = :productId
        """
    )
    fun observeDetails(productId: Long): Flow<ProductDetailsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(details: ProductDetailsEntity)
}
