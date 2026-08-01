package com.dramafy.app.data.model

import com.google.gson.annotations.SerializedName

data class DramaItem(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("cover") val cover: String = "",
    @SerializedName("cover_url") val coverUrl: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("episodes") val episodes: Int = 0,
    @SerializedName("episode_count") val episodeCount: Int = 0,
    @SerializedName("views") val views: Long = 0,
    @SerializedName("likes") val likes: Long = 0,
    @SerializedName("rating") val rating: Double = 0.0,
    @SerializedName("category") val category: String = "",
    @SerializedName("tags") val tags: List<String> = emptyList(),
    @SerializedName("status") val status: String = "",
    @SerializedName("author") val author: String = "",
    @SerializedName("thumbnail") val thumbnail: String = "",
    @SerializedName("horizontal_cover") val horizontalCover: String = "",
    @SerializedName("vertical_cover") val verticalCover: String = "",
    @SerializedName("cast") val cast: List<String> = emptyList(),
    @SerializedName("genre") val genre: List<String> = emptyList(),
    @SerializedName("language") val language: String = "",
    @SerializedName("duration") val duration: String = "",
    @SerializedName("year") val year: Int = 0,
    @SerializedName("is_hot") val isHot: Boolean = false,
    @SerializedName("is_new") val isNew: Boolean = false,
    @SerializedName("is_free") val isFree: Boolean = false,
    @SerializedName("update_status") val updateStatus: String = "",
    @SerializedName("total_episodes") val totalEpisodes: Int = 0,
    @SerializedName("current_episode") val currentEpisode: Int = 0,
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("updated_at") val updatedAt: String = ""
) {
    val displayCover: String
        get() = coverUrl.ifBlank { cover.ifBlank { thumbnail.ifBlank { verticalCover.ifBlank { horizontalCover } } } }
    
    val displayEpisodes: Int
        get() = if (episodeCount > 0) episodeCount else episodes.ifZero(totalEpisodes)
}

private fun Int.ifZero(replacement: Int): Int = if (this == 0) replacement else this

data class SearchResponse(
    @SerializedName("data") val data: List<DramaItem> = emptyList(),
    @SerializedName("list") val list: List<DramaItem> = emptyList(),
    @SerializedName("total") val total: Int = 0,
    @SerializedName("has_more") val hasMore: Boolean = false,
    @SerializedName("error") val error: String? = null
) {
    val items: List<DramaItem>
        get() = data.ifEmpty { list }
}

data class SuggestResponse(
    @SerializedName("data") val data: List<String> = emptyList(),
    @SerializedName("suggestions") val suggestions: List<String> = emptyList(),
    @SerializedName("error") val error: String? = null
) {
    val items: List<String>
        get() = data.ifEmpty { suggestions }
}
