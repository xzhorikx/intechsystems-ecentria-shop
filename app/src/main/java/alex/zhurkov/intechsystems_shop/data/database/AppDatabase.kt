package alex.zhurkov.intechsystems_shop.data.database

import alex.zhurkov.intechsystems_shop.data.database.dao.CategoryDao
import alex.zhurkov.intechsystems_shop.data.database.entity.CategoryEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CategoryEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}
