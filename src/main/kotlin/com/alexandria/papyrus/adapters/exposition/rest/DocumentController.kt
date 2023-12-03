package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.adapters.exposition.rest.DocumentView.Companion.toDocumentView
import com.alexandria.papyrus.application.DocumentUseCases
import com.alexandria.papyrus.domain.model.FileWrapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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

    @Operation(summary = "Download document by Id")
    @GetMapping("/{documentIdentifier}/file")
    fun downloadDocumentByIdentifier(
        @PathVariable documentIdentifier: String,
    ): ResponseEntity<ByteArray> {
        val fileWrapper = documentUseCases.downloadDocumentByIdentifier(documentIdentifier)
        val contentType = fileWrapper.contentType?.let { MediaType.parseMediaType(it) } ?: MediaType.APPLICATION_OCTET_STREAM
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=${fileWrapper.name}")
            .contentType(contentType)
            .body(fileWrapper.content)
    }

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

    @Operation(summary = "Rename Document")
    @PostMapping("/{documentIdentifier}/rename")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun rename(
        @PathVariable documentIdentifier: String,
        @RequestBody renameDocumentRequest: RenameDocumentRequest,
        authentication: Authentication,
    ) {
        documentUseCases.rename(documentIdentifier, renameDocumentRequest.name, authentication.name)
    }

    @Operation(summary = "Change Document Type")
    @PostMapping("/{documentIdentifier}/change-category")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeType(
        @PathVariable documentIdentifier: String,
        @RequestBody changeCategoryRequest: ChangeCategoryRequest,
        authentication: Authentication,
    ) {
        documentUseCases.changeType(documentIdentifier, changeCategoryRequest.categoryIdentifier, authentication.name)
    }

    @Operation(summary = "Change Document Predicted Type")
    @PostMapping("/{documentIdentifier}/change-predicted-category")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePredictedCategory(
        @PathVariable documentIdentifier: String,
        @RequestBody changePredictedCategoryRequest: ChangePredictedCategoryRequest,
        authentication: Authentication,
    ) {
        documentUseCases.changePredictedCategory(documentIdentifier, changePredictedCategoryRequest.categoryIdentifier, authentication.name)
    }

    @Operation(summary = "Change Document Status")
    @PostMapping("/{documentIdentifier}/change-status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeStatus(
        @PathVariable documentIdentifier: String,
        @RequestBody changeStatusRequest: ChangeStatusRequest,
        authentication: Authentication,
    ) {
        documentUseCases.changeStatus(documentIdentifier, changeStatusRequest.status)
    }
}
