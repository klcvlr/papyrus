package com.alexandria.papyrus.infrastructure.api

import com.alexandria.papyrus.application.FolderTemplateUseCases
import com.alexandria.papyrus.infrastructure.api.DetailedFolderTemplateView.Companion.toDetailedFolderTemplateView
import com.alexandria.papyrus.infrastructure.api.FolderTemplateView.Companion.toFolderTemplateView
import org.springframework.http.HttpStatus
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
    fun allFolderTemplates(): List<FolderTemplateView> {
        return folderTemplateUseCases.findAllFolderTemplates().map {
            toFolderTemplateView(it)
        }
    }

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
    ) {
        folderTemplateUseCases.create(createFolderTemplateRequest.folderTemplateName)
    }
}
