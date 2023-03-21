package alex.zhurkov.intechsystems_shop.app.di

import alex.zhurkov.intechsystems_shop.data.database.AppDatabase
import alex.zhurkov.intechsystems_shop.data.database.EntityMapper
import alex.zhurkov.intechsystems_shop.data.database.entity.CategoryEntity
import alex.zhurkov.intechsystems_shop.data.database.entity.ProductEntity
import alex.zhurkov.intechsystems_shop.data.database.mapper.CategoryEntityMapper
import alex.zhurkov.intechsystems_shop.data.database.mapper.ProductEntityMapper
import alex.zhurkov.intechsystems_shop.data.repository.CategoryRepositoryImpl
import alex.zhurkov.intechsystems_shop.data.repository.ProductsRepositoryImpl
import alex.zhurkov.intechsystems_shop.data.source.*
import alex.zhurkov.intechsystems_shop.domain.config.ConfigSource
import alex.zhurkov.intechsystems_shop.domain.model.Category
import alex.zhurkov.intechsystems_shop.domain.model.Product
import alex.zhurkov.intechsystems_shop.domain.repository.CategoriesRepository
import alex.zhurkov.intechsystems_shop.domain.repository.ProductLocalSource
import alex.zhurkov.intechsystems_shop.domain.repository.ProductsRepository
import alex.zhurkov.intechsystems_shop.domain.source.CategoryLocalSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun configSource(): ConfigSource = ConfigSourceImpl()

    @Provides
    @Singleton
    fun categoryEntityMapper(): EntityMapper<Category, CategoryEntity> = CategoryEntityMapper()

    @Provides
    @Singleton
    fun productEntityMapper(): EntityMapper<Product, ProductEntity> = ProductEntityMapper()

    @Provides
    @Singleton
    fun categoryLocalSource(
        database: AppDatabase,
        categoryMapper: EntityMapper<Category, CategoryEntity>,
        configSource: ConfigSource
    ): CategoryLocalSource = CategoryLocalSourceImpl(
        database = database,
        categoryMapper = categoryMapper,
        configSource = configSource
    )

    @Provides
    fun categoryRepository(
        configSource: ConfigSource,
        localSource: CategoryLocalSource,
        remoteSource: CategoryRemoteSource,
    ): CategoriesRepository = CategoryRepositoryImpl(
        configSource = configSource,
        localSource = localSource,
        remoteSource = remoteSource,
    )

    @Provides
    @Singleton
    fun productLocalSource(
        database: AppDatabase,
        productMapper: EntityMapper<Product, ProductEntity>,
        configSource: ConfigSource
    ): ProductLocalSource = ProductLocalSourceImpl(
        database = database,
        productMapper = productMapper,
        configSource = configSource
    )

    @Provides
    fun productRepository(
        configSource: ConfigSource,
        localSource: ProductLocalSource,
        remoteSource: ProductRemoteSource,
    ): ProductsRepository = ProductsRepositoryImpl(
        configSource = configSource,
        localSource = localSource,
        remoteSource = remoteSource,
    )
}
