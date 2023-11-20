package com.alexandria.papyrus.infrastructure.api

import com.alexandria.papyrus.application.FolderUseCases
import com.alexandria.papyrus.infrastructure.api.DetailedFolderView.Companion.toDetailedFolderView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun allFolders(): List<FolderView> {
        return folderUseCases.findAllFolders().map { folder ->
            FolderView(
                identifier = folder.identifier,
                name = folder.name,
                associatedDocumentType = folder.associatedDocumentType?.identifier,
            )
        }
    }

    @GetMapping("/{folderIdentifier}")
    fun folderByIdentifier(
        @PathVariable folderIdentifier: String,
    ): DetailedFolderView {
        val folder = folderUseCases.findByIdentifier(folderIdentifier)
        return toDetailedFolderView(folder)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFromTemplate(
        @RequestBody createFolderFromTemplateRequest: CreateFolderFromTemplateRequest,
    ): ResponseEntity<Unit> {
        val folderIdentifier =
            folderUseCases.createFromTemplate(
                folderTemplateIdentifier = createFolderFromTemplateRequest.templateIdentifier,
            )
        return entityWithLocation(folderIdentifier)
    }
}
