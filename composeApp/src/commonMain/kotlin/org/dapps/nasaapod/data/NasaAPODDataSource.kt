package org.dapps.nasaapod.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.dapps.nasaapod.util.NetworkError
import org.dapps.nasaapod.util.AppResult

interface NasaAPODDataSource {
    suspend fun getAPOD(
        dateISO: String,
    ): AppResult<NasaAPODResponse, NetworkError>
}

class NasaAPODDataSourceImpl(
    private val httpClient: HttpClient
): NasaAPODDataSource {

    override suspend fun getAPOD(
        dateISO: String
    ): AppResult<NasaAPODResponse, NetworkError> {
        val response = try {
            httpClient.get(urlString = Constants.BASE_URL+Constants.APOD_ENDPOINT) {
                parameter("api_key", Constants.API_KEY)
                parameter("date", dateISO)
            }
        } catch(e: UnresolvedAddressException) {
            return AppResult.Error(NetworkError.NO_INTERNET)
        }
        catch (e: SerializationException) {
            return AppResult.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value){
            in 200..299 -> {
                AppResult.Success(response.body<NasaAPODResponse>())
            }
            401 -> AppResult.Error(NetworkError.UNAUTHORIZED)
            403 -> AppResult.Error(NetworkError.UNAUTHORIZED)
            409 -> AppResult.Error(NetworkError.CONFLICT)
            408 -> AppResult.Error(NetworkError.REQUEST_TIMEOUT)
            429 -> AppResult.Error(NetworkError.TOO_MANY_REQUESTS)
            413 -> AppResult.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> AppResult.Error(NetworkError.SERVER_ERROR)
            else -> AppResult.Error(NetworkError.UNKNOWN)
        }
    }

}

