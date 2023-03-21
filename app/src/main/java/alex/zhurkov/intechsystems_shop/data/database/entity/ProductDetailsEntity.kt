package alex.zhurkov.intechsystems_shop.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_details",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class ProductDetailsEntity(
    @PrimaryKey(autoGenerate = false) val productId: Long,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val description: String
)
