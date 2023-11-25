package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.adapters.exposition.rest.DetailedFolderTemplateView.Companion.toDetailedFolderTemplateView
import com.alexandria.papyrus.adapters.exposition.rest.FolderTemplateView.Companion.toFolderTemplateView
import com.alexandria.papyrus.application.FolderTemplateUseCases
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
@RequestMapping("/api/v1/folder-templates")
class FolderTemplateController(private val folderTemplateUseCases: FolderTemplateUseCases) {
    @GetMapping
    fun allFolderTemplates(authentication: Authentication): List<FolderTemplateView> =
        folderTemplateUseCases.findAllRootFolderTemplatesForUser(authentication.name).map { toFolderTemplateView(it) }

    @GetMapping("/{folderTemplateIdentifier}")
    fun folderTemplateByIdentifier(
        @PathVariable folderTemplateIdentifier: String,
    ): DetailedFolderTemplateView {
        val folderTemplate = folderTemplateUseCases.findByIdentifier(folderTemplateIdentifier)
        return toDetailedFolderTemplateView(folderTemplate)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFolderTemplate(
        @RequestBody createFolderTemplateRequest: CreateFolderTemplateRequest,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        val folderTemplateIdentifier = folderTemplateUseCases.create(authentication.name, createFolderTemplateRequest.name)
        return entityWithLocation(folderTemplateIdentifier)
    }

    @PostMapping("/{folderTemplateIdentifier}/create-sub-folder")
    @ResponseStatus(HttpStatus.CREATED)
    fun addSubFolderTemplate(
        @PathVariable folderTemplateIdentifier: String,
        @RequestBody createSubFolderTemplateRequest: CreateSubFolderTemplateRequest,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        val subFolderTemplateIdentifier =
            folderTemplateUseCases.addSubFolder(
                folderTemplateIdentifier,
                createSubFolderTemplateRequest.name,
                authentication.name,
            )
        return entityWithLocation(subFolderTemplateIdentifier)
    }

    @PostMapping("/{folderTemplateIdentifier}/rename")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun rename(
        @PathVariable folderTemplateIdentifier: String,
        @RequestBody renameFolderTemplateRequest: RenameFolderTemplateRequest,
        authentication: Authentication,
    ) {
        folderTemplateUseCases.rename(folderTemplateIdentifier, renameFolderTemplateRequest.name, authentication.name)
    }

    @PostMapping("/{folderTemplateIdentifier}/change-associated-type")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeAssociatedType(
        @PathVariable folderTemplateIdentifier: String,
        @RequestBody changeAssociatedDocumentTypeRequest: ChangeAssociatedDocumentTypeRequest,
        authentication: Authentication,
    ) {
        folderTemplateUseCases.changeAssociatedDocumentType(
            folderTemplateIdentifier,
            changeAssociatedDocumentTypeRequest.typeIdentifier,
            authentication.name,
        )
    }
}
