package com.alexandria.papyrus.infrastructure.api

import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

fun entityWithLocation(resourceId: String): ResponseEntity<Unit> {
    val location =
        ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{resourceId}").buildAndExpand(resourceId).toUri()
    return ResponseEntity.created(location).build()
}
