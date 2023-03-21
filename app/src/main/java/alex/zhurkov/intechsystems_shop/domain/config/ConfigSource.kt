package alex.zhurkov.intechsystems_shop.domain.config

interface ConfigSource {
    val pageSize: Int
    val callTimeOutSec: Long
    val cacheStaleSec: Long
    val cacheSizeByte: Long
    val authKey: String
}