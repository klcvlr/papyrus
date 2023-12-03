package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.adapters.exposition.rest.DetailedFolderTemplateView.Companion.toDetailedFolderTemplateView
import com.alexandria.papyrus.adapters.exposition.rest.FolderTemplateView.Companion.toFolderTemplateView
import com.alexandria.papyrus.application.FolderTemplateUseCases
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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Folder Templates")
@RestController
@RequestMapping("/api/v1/folder-templates")
class FolderTemplateController(private val folderTemplateUseCases: FolderTemplateUseCases) {
    @Operation(summary = "Find all the ROOT Folder Templates")
    @GetMapping
    fun allFolderTemplates(authentication: Authentication): List<FolderTemplateView> =
        folderTemplateUseCases.findAllRootFolderTemplatesForUser(authentication.name).map { toFolderTemplateView(it) }

    @Operation(summary = "Find Folder Template by Id")
    @GetMapping("/{folderTemplateIdentifier}")
    fun folderTemplateByIdentifier(
        @PathVariable folderTemplateIdentifier: String,
    ): DetailedFolderTemplateView {
        val folderTemplate = folderTemplateUseCases.findByIdentifier(folderTemplateIdentifier)
        return toDetailedFolderTemplateView(folderTemplate)
    }

    @Operation(summary = "Create a Folder Template")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFolderTemplate(
        @RequestBody createFolderTemplateRequest: CreateFolderTemplateRequest,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        val folderTemplateIdentifier = folderTemplateUseCases.create(authentication.name, createFolderTemplateRequest.name)
        return entityWithLocation(folderTemplateIdentifier)
    }

    @Operation(summary = "Create a sub-folder inside a Folder Template")
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

    @Operation(summary = "Rename a Folder Template")
    @PostMapping("/{folderTemplateIdentifier}/rename")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun rename(
        @PathVariable folderTemplateIdentifier: String,
        @RequestBody renameFolderTemplateRequest: RenameFolderTemplateRequest,
        authentication: Authentication,
    ) {
        folderTemplateUseCases.rename(folderTemplateIdentifier, renameFolderTemplateRequest.name, authentication.name)
    }

    @Operation(summary = "Change a Folder Template's associated Document Category")
    @PostMapping("/{folderTemplateIdentifier}/change-associated-category")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeAssociatedType(
        @PathVariable folderTemplateIdentifier: String,
        @RequestBody changeAssociatedDocumentCategoryRequest: ChangeAssociatedDocumentCategoryRequest,
        authentication: Authentication,
    ) {
        folderTemplateUseCases.changeAssociatedDocumentCategory(
            folderTemplateIdentifier,
            changeAssociatedDocumentCategoryRequest.categoryIdentifier,
            authentication.name,
        )
    }
}
