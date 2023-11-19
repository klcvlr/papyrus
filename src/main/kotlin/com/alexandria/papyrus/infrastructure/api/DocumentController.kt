package com.alexandria.papyrus.infrastructure.api

import com.alexandria.papyrus.application.DocumentUseCases
import com.alexandria.papyrus.infrastructure.api.DocumentView.Companion.toDocumentView
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/documents")
class DocumentController(private val documentUseCases: DocumentUseCases) {

    @GetMapping
    fun allDocuments(): List<DocumentView> {
        return documentUseCases.findAllDocuments()
            .map { toDocumentView(it) }
    }

    @GetMapping("/{documentIdentifier}")
    fun documentByIdentifier(@PathVariable documentIdentifier: String) {
        documentUseCases.findByIdentifier(documentIdentifier)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createDocument(@RequestBody createDocumentRequest: CreateDocumentRequest) {
        documentUseCases.createDocument(
            createDocumentRequest.documentName,
            createDocumentRequest.parentFolderIdentifier
        )
    }
}
