package com.dramafy.app.data.repository

import com.dramafy.app.data.api.DramaApiService
import com.dramafy.app.data.model.*

class DramaRepository(private val api: DramaApiService) {

    suspend fun getLanguages(): Result<List<Language>> = runCatching {
        val response = api.getLanguages()
        response.data
    }

    suspend fun search(
        query: String,
        lang: String = "en",
        limit: Int = 50,
        offset: Int = 0
    ): Result<List<DramaItem>> = runCatching {
        val response = api.search(query, lang, limit, offset)
        response.items
    }

    suspend fun searchSuggest(
        query: String,
        lang: String = "en"
    ): Result<List<String>> = runCatching {
        val response = api.searchSuggest(query, lang)
        response.items
    }

    suspend fun getBookMall(
        lang: String = "en"
    ): Result<BookMallResponse> = runCatching {
        api.getBookMall(lang)
    }

    suspend fun getBookMallTabs(
        gender: Int = 0,
        lang: String = "en"
    ): Result<List<BookMallTab>> = runCatching {
        val response = api.getBookMallTabs(gender, lang)
        response.items
    }

    suspend fun getBook(
        id: String,
        lang: String = "en"
    ): Result<BookDetail?> = runCatching {
        val response = api.getBook(id, lang)
        response.detail
    }

    suspend fun getSeries(
        id: String,
        lang: String = "en"
    ): Result<SeriesData?> = runCatching {
        val response = api.getSeries(id, lang)
        response.data ?: response.series ?: SeriesData(
            id = id,
            episodes = response.episodes
        )
    }

    suspend fun getMultiVideo(
        id: String,
        lang: String = "en"
    ): Result<MultiVideoData?> = runCatching {
        val response = api.getMultiVideo(id, lang)
        response.data
    }
}
