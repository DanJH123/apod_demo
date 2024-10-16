package org.dapps.nasaapod.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.dapps.nasaapod.domain.NasaAPOD
import org.dapps.nasaapod.util.MediaType

@Serializable
data class NasaAPODResponse (
    @SerialName("copyright")
    val copyright: String? = null,
    @SerialName("date")
    val date: String,
    @SerialName("explanation")
    val explanation: String? = null,
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("hdurl")
    val hdUrl: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("url")
    val url: String,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null
)
fun NasaAPODResponse.toDomain(): NasaAPOD {
    val mediaType = if(this.mediaType == "video")
        MediaType.Video(
            url,
            if (hdUrl.isNullOrEmpty()) null else hdUrl,
            if (thumbnailUrl.isNullOrEmpty()) null else thumbnailUrl
        )
    else MediaType.Image(url, if (hdUrl.isNullOrEmpty()) null else hdUrl)

    return NasaAPOD(
        copyright,
        date,
        explanation,
        mediaType,
        title,
    )
}