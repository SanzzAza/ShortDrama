package com.dramafy.app.data.model

import com.google.gson.annotations.SerializedName

data class BookDetail(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("cover") val cover: String = "",
    @SerializedName("cover_url") val coverUrl: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("synopsis") val synopsis: String = "",
    @SerializedName("episodes") val episodes: Int = 0,
    @SerializedName("episode_count") val episodeCount: Int = 0,
    @SerializedName("total_episodes") val totalEpisodes: Int = 0,
    @SerializedName("views") val views: Long = 0,
    @SerializedName("likes") val likes: Long = 0,
    @SerializedName("rating") val rating: Double = 0.0,
    @SerializedName("category") val category: String = "",
    @SerializedName("tags") val tags: List<String> = emptyList(),
    @SerializedName("genre") val genre: List<String> = emptyList(),
    @SerializedName("status") val status: String = "",
    @SerializedName("author") val author: String = "",
    @SerializedName("cast") val cast: List<String> = emptyList(),
    @SerializedName("thumbnail") val thumbnail: String = "",
    @SerializedName("horizontal_cover") val horizontalCover: String = "",
    @SerializedName("vertical_cover") val verticalCover: String = "",
    @SerializedName("language") val language: String = "",
    @SerializedName("year") val year: Int = 0,
    @SerializedName("is_free") val isFree: Boolean = false,
    @SerializedName("is_hot") val isHot: Boolean = false,
    @SerializedName("is_new") val isNew: Boolean = false,
    @SerializedName("update_status") val updateStatus: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("updated_at") val updatedAt: String = ""
) {
    val displayCover: String
        get() = coverUrl.ifBlank { cover.ifBlank { thumbnail.ifBlank { verticalCover.ifBlank { horizontalCover } } } }
    
    val displayDescription: String
        get() = description.ifBlank { synopsis }
    
    val displayEpisodes: Int
        get() = when {
            episodeCount > 0 -> episodeCount
            totalEpisodes > 0 -> totalEpisodes
            else -> episodes
        }
}

data class BookResponse(
    @SerializedName("data") val data: BookDetail? = null,
    @SerializedName("book") val book: BookDetail? = null,
    @SerializedName("error") val error: String? = null
) {
    val detail: BookDetail?
        get() = data ?: book
}
