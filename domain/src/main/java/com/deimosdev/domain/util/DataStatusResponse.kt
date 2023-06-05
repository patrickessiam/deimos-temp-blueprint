package com.deimosdev.domain.util

data class DataStatusResponse<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): DataStatusResponse<T> {
            return DataStatusResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): DataStatusResponse<T> {
            return DataStatusResponse(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): DataStatusResponse<T> {
            return DataStatusResponse(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}