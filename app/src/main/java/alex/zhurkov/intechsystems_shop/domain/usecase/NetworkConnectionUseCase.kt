package alex.zhurkov.intechsystems_shop.domain.usecase

import kotlinx.coroutines.flow.Flow

interface NetworkConnectionUseCase {
    /**
     * Emits a boolean representing the current state of network connection
     */
    fun observeConnectionState(): Flow<Boolean>
}
