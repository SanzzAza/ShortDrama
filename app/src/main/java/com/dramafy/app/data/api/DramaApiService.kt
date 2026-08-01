package com.dramafy.app.data.api

import com.dramafy.app.data.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface DramaApiService {

    @GET("languages")
    suspend fun getLanguages(): LanguagesResponse

    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("lang") lang: String = "en",
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): SearchResponse

    @GET("search/suggest")
    suspend fun searchSuggest(
        @Query("q") query: String,
        @Query("lang") lang: String = "en"
    ): SuggestResponse

    @GET("bookmall")
    suspend fun getBookMall(
        @Query("lang") lang: String = "en"
    ): BookMallResponse

    @GET("bookmall/tabs")
    suspend fun getBookMallTabs(
        @Query("gender") gender: Int = 0,
        @Query("lang") lang: String = "en"
    ): BookMallTabsResponse

    @GET("book")
    suspend fun getBook(
        @Query("id") id: String,
        @Query("lang") lang: String = "en"
    ): BookResponse

    @GET("series")
    suspend fun getSeries(
        @Query("id") id: String,
        @Query("lang") lang: String = "en"
    ): SeriesResponse

    @GET("multi-video")
    suspend fun getMultiVideo(
        @Query("id") id: String,
        @Query("lang") lang: String = "en"
    ): MultiVideoResponse
}
