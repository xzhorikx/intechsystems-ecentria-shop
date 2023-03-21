package alex.zhurkov.intechsystems_shop.data.source

import alex.zhurkov.intechsystems_shop.BuildConfig
import alex.zhurkov.intechsystems_shop.domain.config.ConfigSource

class ConfigSourceImpl : ConfigSource {
    override val pageSize: Int
        get() = BuildConfig.PAGE_SIZE
    override val callTimeOutSec: Long
        get() = BuildConfig.CALL_TIME_OUT_SEC
    override val cacheStaleSec: Long
        get() = BuildConfig.CACHE_STALE_SEC
    override val cacheSizeByte: Long
        get() = BuildConfig.CACHE_SIZE_BYTE
    override val authKey: String
        get() = BuildConfig.AUTH_KEY
}
