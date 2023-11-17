package com.alexandria.papyrus.domain

import com.alexandria.papyrus.domain.utils.IdGenerator

class FolderTemplateAndFolderService(
    private val folderRepository: FolderRepository,
    private val folderTemplateRepository: FolderTemplateRepository,
    private val idGenerator: IdGenerator
) {
    fun rename(folderTemplate: FolderTemplate, newName: String) {
        folderTemplate.rename(newName)
        val allFoldersCreatedFromThatFolderTemplate = findAllFoldersCreatedFrom(folderTemplate)
        allFoldersCreatedFromThatFolderTemplate.forEach { it.rename(newName) }
        folderRepository.saveAll(allFoldersCreatedFromThatFolderTemplate)
    }

    fun addSubFolderTemplate(folderTemplate: FolderTemplate, subFolderTemplateName: String): String {
        val subFolderTemplate = FolderTemplate(
            identifier = idGenerator.generate(),
            parentFolder = folderTemplate,
            name = subFolderTemplateName,
            associatedDocumentType = null,
        )
        folderTemplate.addSubFolder(subFolderTemplate)
        val allFoldersCreatedFromThatFolderTemplate = findAllFoldersCreatedFrom(folderTemplate)
        allFoldersCreatedFromThatFolderTemplate.forEach {
            val subFolder = Folder(
                identifier = idGenerator.generate(),
                templateIdentifier = folderTemplate.identifier,
                name = subFolderTemplateName
            )
            it.addSubFolder(subFolder)
        }
        folderTemplateRepository.save(subFolderTemplate)
        folderRepository.saveAll(allFoldersCreatedFromThatFolderTemplate)
        return subFolderTemplate.identifier
    }

    fun changeAssociatedDocumentType(folderTemplate: FolderTemplate, newDocumentType: DocumentType) {
        folderTemplate.changeAssociatedDocumentType(newDocumentType)
        val allFoldersCreatedFromThatFolderTemplate = findAllFoldersCreatedFrom(folderTemplate)
        allFoldersCreatedFromThatFolderTemplate.forEach { it.changeAssociatedDocumentType(newDocumentType) }
        folderTemplateRepository.save(folderTemplate)
        folderRepository.saveAll(allFoldersCreatedFromThatFolderTemplate)
    }

    private fun findAllFoldersCreatedFrom(folderTemplate: FolderTemplate): List<Folder> {
        // TODO: This is not efficient, we should have a way to query by template identifier
        return folderRepository.findAll().filter { it.templateIdentifier == folderTemplate.identifier }
    }

}