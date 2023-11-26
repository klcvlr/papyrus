package com.alexandria.papyrus.adapters.exposition.graphql

import com.alexandria.papyrus.application.FolderUseCases
import com.alexandria.papyrus.domain.model.Folder
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller

@Controller
class GraphQLFolderController(private val folderUseCases: FolderUseCases) {
    @QueryMapping
    fun folders(authentication: Authentication): List<Folder> {
        return folderUseCases.findAllFolders()
    }

    @QueryMapping
    fun folderByIdentifier(
        @Argument identifier: String,
        authentication: Authentication,
    ): Folder {
        return folderUseCases.findByIdentifier(identifier, authentication.name)
    }
}
