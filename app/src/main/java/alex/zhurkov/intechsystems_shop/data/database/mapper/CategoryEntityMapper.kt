package alex.zhurkov.intechsystems_shop.data.database.mapper

import alex.zhurkov.intechsystems_shop.data.database.EntityMapper
import alex.zhurkov.intechsystems_shop.data.database.entity.CategoryEntity
import alex.zhurkov.intechsystems_shop.domain.model.Category

class CategoryEntityMapper : EntityMapper<Category, CategoryEntity> {
    override fun toModel(entity: CategoryEntity): Category = Category(
        categoryId = entity.categoryId,
        fullName = entity.fullName,
        url = entity.url
    )

    override fun toEntity(model: Category): CategoryEntity = CategoryEntity(
        categoryId = model.categoryId,
        fullName = model.fullName,
        url = model.url
    )
}
