package alex.zhurkov.intechsystems_shop.data.database

interface EntityMapper<Model, Entity> {
    fun toModel(entity: Entity): Model
    fun toEntity(model: Model): Entity
}