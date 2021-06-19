package com.mcecelja.catalogue.data.dto.places

data class PlacesResponseDTO(
    val html_attributions: List<Any>,
    val results: List<PlaceDTO>
)