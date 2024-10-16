package org.dapps.nasaapod.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.composeapp.generated.resources.Res
import apod.composeapp.generated.resources.error_conflict
import apod.composeapp.generated.resources.error_date_format
import apod.composeapp.generated.resources.error_no_internet
import apod.composeapp.generated.resources.error_payload_too_large
import apod.composeapp.generated.resources.error_request_timeout
import apod.composeapp.generated.resources.error_serialization
import apod.composeapp.generated.resources.error_server_error
import apod.composeapp.generated.resources.error_too_many_requests
import apod.composeapp.generated.resources.error_unauthorized
import apod.composeapp.generated.resources.error_unknown
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.dapps.nasaapod.domain.ApodError
import org.dapps.nasaapod.domain.NasaAPOD
import org.dapps.nasaapod.domain.usecases.FetchApod
import org.dapps.nasaapod.util.AppResult
import org.dapps.nasaapod.util.IError
import org.dapps.nasaapod.util.MediaType
import org.dapps.nasaapod.util.NetworkError
import org.jetbrains.compose.resources.StringResource

class ApodUiModel (
    val copyright: String?,
    val date: String,
    val explanation: String?,
    val mediaType: MediaType,
    val title: String,
)

fun NasaAPOD.toUi(): ApodUiModel {
    return ApodUiModel(
        copyright = this.copyright,
        date = this.date,
        explanation = this.explanation,
        mediaType = this.mediaType,
        title = this.title ?: date,
    )
}

sealed class ApodUiState {
    data object Initial : ApodUiState()
    data object Loading : ApodUiState()
    data class Success(val apod: ApodUiModel) : ApodUiState()
    data class Error(val message: StringResource) : ApodUiState()
}

class ApodViewModel(
    private val fetchApod: FetchApod,
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(
        Clock.System.todayIn(TimeZone.currentSystemDefault())
    )
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _apodUiState = MutableStateFlow<ApodUiState>(ApodUiState.Initial)
    val apodUiState: StateFlow<ApodUiState> = _apodUiState
        .onStart {
            loadApod()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ApodUiState.Initial
        )

    fun reload(){
        _apodUiState.value = ApodUiState.Initial
        loadApod()
    }

    private fun loadApod() {
        viewModelScope.launch {
            _apodUiState.value = ApodUiState.Loading

            val date = _selectedDate.value
            when (val apodResult = fetchApod(date)) {
                is AppResult.Success -> {
                    _apodUiState.value = ApodUiState.Success(apodResult.data.toUi())
                }
                is AppResult.Error -> {
                    val errorMessageResId = getErrorMessageResId(apodResult.error)
                    _apodUiState.value = ApodUiState.Error(errorMessageResId)
                }
            }
        }
    }

    fun nextDay(){
        // check if current date is today, if so, do nothing
        if(_selectedDate.value == Clock.System.todayIn(TimeZone.currentSystemDefault())){
            return
        }
        _selectedDate.value = _selectedDate.value.plus(1, DateTimeUnit.DAY)
        loadApod()
    }

    fun previousDay(){
        _selectedDate.value = _selectedDate.value.minus(1, DateTimeUnit.DAY)
        loadApod()
    }

    private fun getErrorMessageResId(error: IError): StringResource {
        return when (error) {
            NetworkError.REQUEST_TIMEOUT -> Res.string.error_request_timeout
            NetworkError.UNAUTHORIZED -> Res.string.error_unauthorized
            NetworkError.CONFLICT -> Res.string.error_conflict
            NetworkError.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
            NetworkError.NO_INTERNET -> Res.string.error_no_internet
            NetworkError.PAYLOAD_TOO_LARGE -> Res.string.error_payload_too_large
            NetworkError.SERVER_ERROR -> Res.string.error_server_error
            NetworkError.SERIALIZATION -> Res.string.error_serialization
            ApodError.INVALID_DATE_FORMAT -> Res.string.error_date_format
            else -> Res.string.error_unknown
        }
    }
}
