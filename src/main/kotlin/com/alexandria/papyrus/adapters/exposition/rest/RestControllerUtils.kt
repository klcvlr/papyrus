package com.alexandria.papyrus.adapters.exposition.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

fun entityWithLocation(resourceId: String): ResponseEntity<Unit> {
    // FIXME: This works only on base resources path (appends too much on sub-resources)
    val location =
        ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{resourceId}").buildAndExpand(resourceId).toUri()
    return ResponseEntity.created(location).build()
}
