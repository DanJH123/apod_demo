package org.dapps.nasaapod.util

sealed class MediaType(val url: String, val hdUrl: String?)  {
    class Image(url: String, hdUrl: String?) : MediaType(url, hdUrl)
    class Video(url: String, hdUrl: String?, val thumbnailUrl: String?) : MediaType(url, hdUrl)
}