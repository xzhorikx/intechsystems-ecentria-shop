package alex.zhurkov.intechsystems_shop.app.di

import alex.zhurkov.intechsystems_shop.data.usecase.NetworkConnectionUseCaseImpl
import alex.zhurkov.intechsystems_shop.domain.usecase.NetworkConnectionUseCase
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class NetworkConnectionModule {
    @Provides
    fun networkConnectionUseCase(
        @AppContext context: Context
    ): NetworkConnectionUseCase = NetworkConnectionUseCaseImpl(
        context = context
    )
}
