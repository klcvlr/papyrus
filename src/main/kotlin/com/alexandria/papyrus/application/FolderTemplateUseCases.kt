package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.DocumentType
import com.alexandria.papyrus.domain.FolderTemplate
import com.alexandria.papyrus.domain.FolderTemplateAndFolderService
import com.alexandria.papyrus.domain.FolderTemplateRepository
import com.alexandria.papyrus.domain.exceptions.FolderTemplateNotFoundException
import com.alexandria.papyrus.domain.utils.IdGenerator
import org.springframework.transaction.annotation.Transactional

@Transactional
class FolderTemplateUseCases(
    private val idGenerator: IdGenerator,
    private val folderTemplateAndFolderService: FolderTemplateAndFolderService,
    private val folderTemplateRepository: FolderTemplateRepository,
) {

    // ------------------ PUBLIC ------------------
    fun create(name: String): String {
        val folderTemplate = FolderTemplate(
            identifier = idGenerator.generate(),
            name = name,
            parentFolder = null,
            associatedDocumentType = null,
        )
        folderTemplateRepository.save(folderTemplate)
        return folderTemplate.identifier
    }

    fun rename(identifier: String, newName: String) {
        val folderTemplate = findByIdentifier(identifier)
        folderTemplateAndFolderService.rename(folderTemplate, newName)
        folderTemplateRepository.save(folderTemplate)
    }

    fun addSubFolder(identifier: String, subFolderName: String): String {
        val folderTemplate = findByIdentifier(identifier)
        return folderTemplateAndFolderService.addSubFolderTemplate(folderTemplate, subFolderName)
    }

    fun changeAssociatedDocumentType(identifier: String, newDocumentType: DocumentType) {
        val folderTemplate = findByIdentifier(identifier)
        folderTemplateAndFolderService.changeAssociatedDocumentType(folderTemplate, newDocumentType)
    }

    // ------------------ PRIVATE ------------------
    private fun findByIdentifier(identifier: String): FolderTemplate =
        folderTemplateRepository.findByIdentifier(identifier) ?: throw FolderTemplateNotFoundException(identifier)
}
