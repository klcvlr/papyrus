package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.adapters.exposition.rest.DocumentTypeView.Companion.toDocumentTypeView
import com.alexandria.papyrus.application.DocumentTypeUseCases
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
@RequestMapping("/api/v1/document-types")
class DocumentTypeController(private val documentTypeUseCases: DocumentTypeUseCases) {
    @Operation(summary = "Find Document Type by Id")
    @GetMapping("/{documentTypeIdentifier}")
    fun documentTypeByIdentifier(
        @PathVariable documentTypeIdentifier: String,
    ): DocumentTypeView {
        val documentType = documentTypeUseCases.findByIdentifier(documentTypeIdentifier)
        return toDocumentTypeView(documentType)
    }

    @Operation(summary = "Create a Document Type")
    @PostMapping
    fun create(
        @RequestBody createDocumentTypeRequest: CreateDocumentTypeRequest,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        val documentTypeId = documentTypeUseCases.create(createDocumentTypeRequest.name, authentication.name)
        return entityWithLocation(documentTypeId)
    }
}
