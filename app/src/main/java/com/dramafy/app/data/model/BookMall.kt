package com.dramafy.app.data.model

import com.google.gson.annotations.SerializedName

data class BookMallTab(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("order") val order: Int = 0
)

data class BookMallTabsResponse(
    @SerializedName("data") val data: List<BookMallTab> = emptyList(),
    @SerializedName("tabs") val tabs: List<BookMallTab> = emptyList(),
    @SerializedName("error") val error: String? = null
) {
    val items: List<BookMallTab>
        get() = data.ifEmpty { tabs }
}

data class BookMallSection(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("list") val list: List<DramaItem> = emptyList(),
    @SerializedName("data") val data: List<DramaItem> = emptyList(),
    @SerializedName("items") val items: List<DramaItem> = emptyList(),
    @SerializedName("banners") val banners: List<DramaItem> = emptyList()
) {
    val displayItems: List<DramaItem>
        get() = list.ifEmpty { data.ifEmpty { items } }
}

data class BookMallResponse(
    @SerializedName("data") val data: List<BookMallSection> = emptyList(),
    @SerializedName("sections") val apiSections: List<BookMallSection> = emptyList(),
    @SerializedName("banners") val banners: List<DramaItem> = emptyList(),
    @SerializedName("error") val error: String? = null
) {
    val resolvedSections: List<BookMallSection>
        get() = data.ifEmpty { apiSections }
}
