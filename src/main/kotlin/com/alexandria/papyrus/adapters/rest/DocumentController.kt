package com.alexandria.papyrus.adapters.rest

import com.alexandria.papyrus.application.DocumentUseCases
import com.alexandria.papyrus.adapters.rest.DocumentView.Companion.toDocumentView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
class DocumentController(private val documentUseCases: DocumentUseCases) {
    @GetMapping("/{documentIdentifier}")
    fun documentByIdentifier(
        @PathVariable documentIdentifier: String,
    ): DocumentView {
        val document = documentUseCases.findByIdentifier(documentIdentifier)
        return toDocumentView(document)
    }

    @PostMapping(consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestPart createDocumentRequest: CreateDocumentRequest,
        @RequestPart file: MultipartFile,
    ): ResponseEntity<Unit> {
        val documentIdentifier =
            documentUseCases.createDocument(
                createDocumentRequest.name,
                createDocumentRequest.folderIdentifier,
            )
        return entityWithLocation(documentIdentifier)
    }

    @PostMapping("/{documentIdentifier}/change-type")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeType(
        @PathVariable documentIdentifier: String,
        @RequestBody changeTypeRequest: ChangeTypeRequest,
    ) {
        documentUseCases.changeType(documentIdentifier, changeTypeRequest.typeIdentifier)
    }

    @PostMapping("/{documentIdentifier}/change-predicted-type")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePredictedType(
        @PathVariable documentIdentifier: String,
        @RequestBody changePredictedTypeRequest: ChangePredictedTypeRequest,
    ) {
        documentUseCases.changePredictedType(documentIdentifier, changePredictedTypeRequest.typeIdentifier)
    }
}
