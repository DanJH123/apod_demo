package org.dapps.nasaapod.data

import org.dapps.nasaapod.domain.ApodRepository
import org.dapps.nasaapod.domain.NasaAPOD
import org.dapps.nasaapod.util.AppResult
import org.dapps.nasaapod.util.NetworkError

class ApodRepositoryImpl(private val remoteDataSource: NasaAPODDataSource): ApodRepository {

    override suspend fun fetchApodFromApi(
        dateYYYYMMDD: String
    ): AppResult<NasaAPOD, NetworkError> {
        return when (val result = remoteDataSource.getAPOD(dateYYYYMMDD)) {
            is AppResult.Success -> AppResult.Success(result.data.toDomain())
            is AppResult.Error -> result
        }
    }
}

