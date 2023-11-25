package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.adapters.exposition.rest.DetailedFolderView.Companion.toDetailedFolderView
import com.alexandria.papyrus.application.FolderUseCases
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/folders")
class FolderController(private val folderUseCases: FolderUseCases) {
    @GetMapping
    fun allFolders(): List<FolderView> =
        folderUseCases.findAllFolders().map {
                folder ->
            FolderView(
                identifier = folder.identifier,
                name = folder.name,
                associatedDocumentType = folder.associatedDocumentType?.identifier,
            )
        }

    @GetMapping("/{folderIdentifier}")
    fun folderByIdentifier(
        @PathVariable folderIdentifier: String,
        authentication: Authentication,
    ): DetailedFolderView = toDetailedFolderView(folderUseCases.findByIdentifier(folderIdentifier, authentication.name))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFromTemplate(
        @RequestBody createFolderFromTemplateRequest: CreateFolderFromTemplateRequest,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        val folderIdentifier =
            folderUseCases.createFromTemplate(
                folderTemplateIdentifier = createFolderFromTemplateRequest.templateIdentifier,
                user = authentication.name,
            )
        return entityWithLocation(folderIdentifier)
    }
}
