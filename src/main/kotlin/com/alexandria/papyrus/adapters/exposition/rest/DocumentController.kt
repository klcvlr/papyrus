package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.adapters.exposition.rest.DocumentView.Companion.toDocumentView
import com.alexandria.papyrus.application.DocumentUseCases
import com.alexandria.papyrus.domain.model.FileWrapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/documents")
@Tag(name = "Documents")
class DocumentController(private val documentUseCases: DocumentUseCases) {
    @Operation(summary = "Find document by Id")
    @GetMapping("/{documentIdentifier}")
    fun documentByIdentifier(
        @PathVariable documentIdentifier: String,
    ): DocumentView = toDocumentView(documentUseCases.findByIdentifier(documentIdentifier))

    @Operation(summary = "Create a document")
    @PostMapping(consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestPart createDocumentRequest: CreateDocumentRequest,
        @RequestPart file: MultipartFile,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        val fileWrapper = FileWrapper(name = createDocumentRequest.name, content = file.bytes)
        val documentIdentifier = documentUseCases.createDocument(createDocumentRequest.folderIdentifier, fileWrapper, authentication.name)
        return entityWithLocation(documentIdentifier)
    }

    @Operation(summary = "Change Document Type")
    @PostMapping("/{documentIdentifier}/change-type")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeType(
        @PathVariable documentIdentifier: String,
        @RequestBody changeTypeRequest: ChangeTypeRequest,
        authentication: Authentication,
    ) {
        documentUseCases.changeType(documentIdentifier, changeTypeRequest.typeIdentifier, authentication.name)
    }

    @Operation(summary = "Change Document Predicted Type")
    @PostMapping("/{documentIdentifier}/change-predicted-type")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePredictedType(
        @PathVariable documentIdentifier: String,
        @RequestBody changePredictedTypeRequest: ChangePredictedTypeRequest,
        authentication: Authentication,
    ) {
        documentUseCases.changePredictedType(documentIdentifier, changePredictedTypeRequest.typeIdentifier, authentication.name)
    }
}
