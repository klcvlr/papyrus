package com.alexandria.papyrus.domain.services

import com.alexandria.papyrus.domain.FolderTemplateNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.model.Folder
import com.alexandria.papyrus.domain.model.FolderTemplate
import com.alexandria.papyrus.domain.repositories.DocumentTypeRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository
import com.alexandria.papyrus.domain.repositories.FolderTemplateRepository

class FolderTemplateAndFolderService(
    private val idGenerator: IdGenerator,
    private val folderRepository: FolderRepository,
    private val folderTemplateRepository: FolderTemplateRepository,
    private val documentTypeRepository: DocumentTypeRepository,
) {
    fun rename(
        folderTemplate: FolderTemplate,
        newName: String,
    ) {
        folderTemplate.rename(newName)
        val allFoldersCreatedFromThatFolderTemplate = findAllFoldersCreatedFrom(folderTemplate)
        allFoldersCreatedFromThatFolderTemplate.forEach { it.rename(newName) }
        folderRepository.saveAll(allFoldersCreatedFromThatFolderTemplate)
    }

    fun addSubFolderTemplate(
        folderTemplate: FolderTemplate,
        subFolderTemplateName: String,
        userIdentifier: String,
    ): String {
        val subFolderTemplate =
            FolderTemplate(
                identifier = idGenerator.generate(),
                parentFolder = folderTemplate,
                name = subFolderTemplateName,
                associatedDocumentType = null,
                user = userIdentifier,
            )
        folderTemplate.addSubFolder(subFolderTemplate)
        val allFoldersCreatedFromThatFolderTemplate = findAllFoldersCreatedFrom(folderTemplate)
        allFoldersCreatedFromThatFolderTemplate.forEach {
            val subFolder =
                Folder(
                    identifier = idGenerator.generate(),
                    template = folderTemplate,
                    name = subFolderTemplateName,
                    user = userIdentifier,
                )
            it.addSubFolder(subFolder)
        }
        folderTemplateRepository.save(subFolderTemplate)
        folderRepository.saveAll(allFoldersCreatedFromThatFolderTemplate)
        return subFolderTemplate.identifier
    }

    fun changeAssociatedDocumentType(
        folderTemplateIdentifier: String,
        newDocumentTypeIdentifier: String,
        userIdentifier: String,
    ) {
        val folderTemplate =
            folderTemplateRepository.findByIdentifier(folderTemplateIdentifier)
                ?: throw FolderTemplateNotFoundException(
                    folderTemplateIdentifier,
                )
        val newDocumentType =
            documentTypeRepository.findByIdentifier(newDocumentTypeIdentifier) ?: throw FolderTemplateNotFoundException(
                newDocumentTypeIdentifier,
            )
        folderTemplate.changeAssociatedDocumentType(newDocumentType)
        val allFoldersCreatedFromThatFolderTemplate = findAllFoldersCreatedFrom(folderTemplate)
        allFoldersCreatedFromThatFolderTemplate.forEach { it.changeAssociatedDocumentType(newDocumentType) }
        folderTemplateRepository.save(folderTemplate)
        folderRepository.saveAll(allFoldersCreatedFromThatFolderTemplate)
    }

    fun createFolderFromTemplate(
        folderTemplate: FolderTemplate,
        userIdentifier: String,
    ): Folder {
        return if (folderTemplate.subFolders.isEmpty()) {
            Folder(
                identifier = idGenerator.generate(),
                template = folderTemplate,
                name = folderTemplate.name,
                associatedDocumentType = folderTemplate.associatedDocumentType,
                user = userIdentifier,
            )
        } else {
            val subFolders = folderTemplate.subFolders.map { createFolderFromTemplate(it, userIdentifier) }
            val folder =
                Folder(
                    identifier = idGenerator.generate(),
                    template = folderTemplate,
                    name = folderTemplate.name,
                    associatedDocumentType = folderTemplate.associatedDocumentType,
                    user = userIdentifier,
                )
            subFolders.forEach { folder.addSubFolder(it) }
            folder
        }
    }

    private fun findAllFoldersCreatedFrom(folderTemplate: FolderTemplate): List<Folder> =
        folderRepository.findAllByTemplate(folderTemplate.identifier)
}
