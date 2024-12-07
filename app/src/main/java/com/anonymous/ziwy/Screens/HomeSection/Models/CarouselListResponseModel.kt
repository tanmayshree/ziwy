package com.anonymous.ziwy.Screens.HomeSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class CarouselListResponseModel(
    val statusCode: Int? = null,
    val body: ArrayList<Carousel>? = arrayListOf(),
    val message: String? = null
)

@Serializable
data class Carousel(
    val imageLink: String? = null,
    val id: String? = null,
    val caption: String? = null,
    val productLink: String? = null
)
