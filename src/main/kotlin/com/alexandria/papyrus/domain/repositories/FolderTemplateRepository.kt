package com.alexandria.papyrus.domain.repositories

import com.alexandria.papyrus.domain.model.FolderTemplate

interface FolderTemplateRepository {
    fun findByIdentifier(identifier: String): FolderTemplate?

    fun findAllRootFolderTemplatesForUser(userIdentifier: String): List<FolderTemplate>

    fun save(folderTemplate: FolderTemplate)
}
