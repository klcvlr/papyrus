package com.alexandria.papyrus.domain

interface FolderTemplateRepository {
    fun save(folderTemplate: FolderTemplate)
    fun findByIdentifier(identifier: String): FolderTemplate?
}