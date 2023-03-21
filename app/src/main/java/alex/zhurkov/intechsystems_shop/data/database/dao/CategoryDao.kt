package alex.zhurkov.intechsystems_shop.data.database.dao

import alex.zhurkov.intechsystems_shop.data.database.entity.CategoryEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {

    @Query(
        """
        SELECT * FROM category 
        LIMIT :limit OFFSET :offset
        """
    )
    suspend fun getCategories(limit: Int, offset: Int): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(categories: List<CategoryEntity>)
}
