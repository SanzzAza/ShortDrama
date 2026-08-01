package com.dramafy.app.data.model

import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("episode") val episode: Int = 0,
    @SerializedName("number") val number: Int = 0,
    @SerializedName("index") val index: Int = 0,
    @SerializedName("video_url") val videoUrl: String = "",
    @SerializedName("video") val video: String = "",
    @SerializedName("url") val url: String = "",
    @SerializedName("cover") val cover: String = "",
    @SerializedName("thumbnail") val thumbnail: String = "",
    @SerializedName("cover_url") val coverUrl: String = "",
    @SerializedName("duration") val duration: Long = 0,
    @SerializedName("duration_text") val durationText: String = "",
    @SerializedName("is_free") val isFree: Boolean = false,
    @SerializedName("is_locked") val isLocked: Boolean = false,
    @SerializedName("coins") val coins: Int = 0,
    @SerializedName("views") val views: Long = 0,
    @SerializedName("likes") val likes: Long = 0,
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("description") val description: String = ""
) {
    val displayVideoUrl: String
        get() = videoUrl.ifBlank { video.ifBlank { url } }
    
    val displayCover: String
        get() = coverUrl.ifBlank { cover.ifBlank { thumbnail } }
    
    val displayName: String
        get() = title.ifBlank { name.ifBlank { "Episode ${displayEpisode}" } }
    
    val displayEpisode: Int
        get() = when {
            episode > 0 -> episode
            number > 0 -> number
            else -> index
        }
}

data class SeriesData(
    @SerializedName("id") val id: String = "",
    @SerializedName("book_id") val bookId: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("episodes") val episodes: List<Episode> = emptyList(),
    @SerializedName("list") val list: List<Episode> = emptyList(),
    @SerializedName("data") val data: List<Episode> = emptyList(),
    @SerializedName("total") val total: Int = 0,
    @SerializedName("has_more") val hasMore: Boolean = false
) {
    val episodeList: List<Episode>
        get() = episodes.ifEmpty { list.ifEmpty { data } }
}

data class SeriesResponse(
    @SerializedName("data") val data: SeriesData? = null,
    @SerializedName("series") val series: SeriesData? = null,
    @SerializedName("episodes") val episodes: List<Episode> = emptyList(),
    @SerializedName("error") val error: String? = null
)

data class MultiVideoData(
    @SerializedName("id") val id: String = "",
    @SerializedName("videos") val videos: List<VideoItem> = emptyList(),
    @SerializedName("list") val list: List<VideoItem> = emptyList(),
    @SerializedName("data") val data: List<VideoItem> = emptyList(),
    @SerializedName("total") val total: Int = 0
)

data class VideoItem(
    @SerializedName("id") val id: String = "",
    @SerializedName("url") val url: String = "",
    @SerializedName("video_url") val videoUrl: String = "",
    @SerializedName("video") val video: String = "",
    @SerializedName("cover") val cover: String = "",
    @SerializedName("thumbnail") val thumbnail: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("duration") val duration: Long = 0,
    @SerializedName("quality") val quality: String = "",
    @SerializedName("episode") val episode: Int = 0,
    @SerializedName("is_free") val isFree: Boolean = false
) {
    val displayUrl: String
        get() = videoUrl.ifBlank { url.ifBlank { video } }
}

data class MultiVideoResponse(
    @SerializedName("data") val data: MultiVideoData? = null,
    @SerializedName("error") val error: String? = null
)
