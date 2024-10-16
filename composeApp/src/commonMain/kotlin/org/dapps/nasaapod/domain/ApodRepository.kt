package org.dapps.nasaapod.domain

import org.dapps.nasaapod.util.AppResult
import org.dapps.nasaapod.util.NetworkError

interface ApodRepository {
    suspend fun fetchApodFromApi(
        dateYYYYMMDD: String
    ): AppResult<NasaAPOD, NetworkError>
}