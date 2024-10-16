package org.dapps.nasaapod.util

interface IError

enum class NetworkError : IError {
    REQUEST_TIMEOUT,
    UNAUTHORIZED,
    CONFLICT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    PAYLOAD_TOO_LARGE,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN;
}

sealed interface AppResult<out D, out E: IError> {
    data class Success<out D>(val data: D): AppResult<D, Nothing>
    data class Error<out E: IError>(val error: E): AppResult<Nothing, E>
}

inline fun <T, E: IError, R> AppResult<T, E>.map(map: (T) -> R): AppResult<R, E> {
    return when(this) {
        is AppResult.Error -> AppResult.Error(error)
        is AppResult.Success -> AppResult.Success(map(data))
    }
}

fun <T, E: IError> AppResult<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

inline fun <T, E: IError> AppResult<T, E>.onSuccess(action: (T) -> Unit): AppResult<T, E> {
    return when(this) {
        is AppResult.Error -> this
        is AppResult.Success -> {
            action(data)
            this
        }
    }
}
inline fun <T, E: IError> AppResult<T, E>.onError(action: (E) -> Unit): AppResult<T, E> {
    return when(this) {
        is AppResult.Error -> {
            action(error)
            this
        }
        is AppResult.Success -> this
    }
}

typealias EmptyResult<E> = AppResult<Unit, E>