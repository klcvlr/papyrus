package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.adapters.exposition.rest.DocumentCategoryView.Companion.toDocumentCategoryView
import com.alexandria.papyrus.application.DocumentCategoryUseCases
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Document Types")
@RestController
@RequestMapping("/api/v1/document-categories")
class DocumentCategoryController(private val documentCategoryUseCases: DocumentCategoryUseCases) {
    @Operation(summary = "Find Document Type by Id")
    @GetMapping("/{documentCategoryIdentifier}")
    fun documentCategoryByIdentifier(
        @PathVariable documentCategoryIdentifier: String,
    ): DocumentCategoryView {
        val documentCategory = documentCategoryUseCases.findByIdentifier(documentCategoryIdentifier)
        return toDocumentCategoryView(documentCategory)
    }

    @Operation(summary = "Create a Document Type")
    @PostMapping
    fun create(
        @RequestBody createDocumentCategoryRequest: CreateDocumentCategoryRequest,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        val documentCategoryId = documentCategoryUseCases.create(createDocumentCategoryRequest.name, authentication.name)
        return entityWithLocation(documentCategoryId)
    }
}
