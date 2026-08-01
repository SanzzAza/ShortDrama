package com.dramafy.app.data.model

data class Language(
    val code: String = "",
    val name: String = "",
    val nativeName: String = ""
)

data class LanguagesResponse(
    val data: List<Language> = emptyList(),
    val error: String? = null
)
