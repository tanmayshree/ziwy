package com.anonymous.ziwy.GenericModels

import kotlinx.serialization.Serializable

@Serializable
data class CountriesListModel(
    val countriesList: List<Country>? = null
)


@Serializable
data class Country(
    val name: String? = null,
    val currency: String? = null,
    val unicodeFlag: String? = null,
    val dialcode: String? = null
)