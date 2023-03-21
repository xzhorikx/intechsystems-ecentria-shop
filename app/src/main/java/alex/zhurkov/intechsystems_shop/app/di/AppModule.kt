package alex.zhurkov.intechsystems_shop.app.di

import alex.zhurkov.intechsystems_shop.app.MainApplication
import alex.zhurkov.intechsystems_shop.data.database.AppDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @AppContext
    fun appContext(app: MainApplication): Context = app.applicationContext


    @Provides
    @Singleton
    fun appDatabase(@AppContext context: Context): AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "app_database"
    ).fallbackToDestructiveMigration().build()
}
