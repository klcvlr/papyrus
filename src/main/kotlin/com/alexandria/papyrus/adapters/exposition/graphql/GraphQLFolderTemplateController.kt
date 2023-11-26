package com.alexandria.papyrus.adapters.exposition.graphql

import com.alexandria.papyrus.application.FolderTemplateUseCases
import com.alexandria.papyrus.domain.model.FolderTemplate
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller

@Controller
class GraphQLFolderTemplateController(private val folderTemplateUseCases: FolderTemplateUseCases) {
    @QueryMapping
    fun folderTemplates(authentication: Authentication): List<FolderTemplate> =
        folderTemplateUseCases.findAllRootFolderTemplatesForUser(authentication.name)

    @QueryMapping
    fun folderTemplateByIdentifier(
        @Argument identifier: String,
        authentication: Authentication,
    ): FolderTemplate {
        return folderTemplateUseCases.findByIdentifier(identifier)
    }
}
