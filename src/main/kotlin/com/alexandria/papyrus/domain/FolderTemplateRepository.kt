package com.alexandria.papyrus.domain

interface FolderTemplateRepository {
    fun findByIdentifier(identifier: String): FolderTemplate
    fun save(folderTemplate: FolderTemplate)
}