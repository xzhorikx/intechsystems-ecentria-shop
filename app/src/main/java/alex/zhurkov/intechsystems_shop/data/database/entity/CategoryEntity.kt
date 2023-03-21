package alex.zhurkov.intechsystems_shop.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false) val categoryId: String,
    val fullName: String,
    val url: String
)
