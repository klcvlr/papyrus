package com.alexandria.papyrus.infrastructure.api

import com.alexandria.papyrus.application.DocumentTypeUseCases
import com.alexandria.papyrus.domain.model.DocumentType
import com.alexandria.papyrus.infrastructure.api.DocumentTypeView.Companion.toDocumentTypeView
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/document-types")
class DocumentTypeController(private val documentTypeUseCases: DocumentTypeUseCases) {
    @GetMapping("/{documentTypeIdentifier}")
    fun documentTypeByIdentifier(
        @PathVariable documentTypeIdentifier: String,
    ): DocumentTypeView {
        val documentType = documentTypeUseCases.findByIdentifier(documentTypeIdentifier)
        return toDocumentTypeView(documentType)
    }

    @PostMapping
    fun create(
        @RequestBody createDocumentTypeRequest: CreateDocumentTypeRequest,
    ): ResponseEntity<Unit> {
        val documentTypeId = documentTypeUseCases.create(createDocumentTypeRequest.name)
        return entityWithLocation(documentTypeId)
    }
}