package com.mcecelja.catalogue.data.dto.places

data class CoordinatesDTO(
    val lat: Double,
    val lng: Double
) {
    override fun toString(): String {
        return String.format("%s,%s", lat, lng)
    }
}