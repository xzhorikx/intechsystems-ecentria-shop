package alex.zhurkov.intechsystems_shop.data.database

import alex.zhurkov.intechsystems_shop.data.database.dao.CategoryDao
import alex.zhurkov.intechsystems_shop.data.database.dao.ProductDao
import alex.zhurkov.intechsystems_shop.data.database.dao.ProductDetailsDao
import alex.zhurkov.intechsystems_shop.data.database.entity.CategoryEntity
import alex.zhurkov.intechsystems_shop.data.database.entity.ProductDetailsEntity
import alex.zhurkov.intechsystems_shop.data.database.entity.ProductEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class,
        ProductDetailsEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun productDetailsDao(): ProductDetailsDao
}
