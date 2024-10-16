package org.dapps.nasaapod.domain

import org.dapps.nasaapod.util.MediaType

data class NasaAPOD (
    val copyright: String?,
    val date: String,
    val explanation: String?,
    val mediaType: MediaType,
    val title: String?,
)