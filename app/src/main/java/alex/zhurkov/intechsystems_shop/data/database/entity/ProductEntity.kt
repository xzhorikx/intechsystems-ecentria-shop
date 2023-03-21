package alex.zhurkov.intechsystems_shop.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "product",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
class ProductEntity(
    @PrimaryKey(autoGenerate = false) val productId: Long,
    val categoryId: String,
    val url: String,
    val name: String,
    val imageUrl: String,
    val listPrice: Double
)
