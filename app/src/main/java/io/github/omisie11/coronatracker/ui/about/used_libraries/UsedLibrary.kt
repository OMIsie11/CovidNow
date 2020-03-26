package io.github.omisie11.coronatracker.ui.about.used_libraries

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsedLibrary(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "license")
    val license: String,
    @field:Json(name = "repositoryUrl")
    val repositoryUrl: String
)
