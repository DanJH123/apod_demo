package org.dapps.nasaapod.domain.usecases

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import org.dapps.nasaapod.domain.ApodError
import org.dapps.nasaapod.domain.ApodRepository
import org.dapps.nasaapod.domain.NasaAPOD
import org.dapps.nasaapod.util.AppResult
import org.dapps.nasaapod.util.IError
import org.dapps.nasaapod.util.NetworkError

class FetchApod (private val repository: ApodRepository){

    suspend operator fun invoke (date: LocalDate): AppResult<NasaAPOD, IError> {
        try {
            val formattedDate = date.format(LocalDate.Formats.ISO)
            return repository.fetchApodFromApi(formattedDate)
        }
        catch (e: IllegalStateException){
            return AppResult.Error(ApodError.INVALID_DATE_FORMAT)
        }
        catch (e: Exception){
            return AppResult.Error(NetworkError.UNKNOWN)
        }
    }
}