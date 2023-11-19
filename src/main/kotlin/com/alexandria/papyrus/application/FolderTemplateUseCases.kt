package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.model.DocumentType
import com.alexandria.papyrus.domain.model.FolderTemplate
import com.alexandria.papyrus.domain.repositories.FolderTemplateRepository
import com.alexandria.papyrus.domain.services.FolderTemplateAndFolderService
import org.springframework.transaction.annotation.Transactional

@Transactional
class FolderTemplateUseCases(
    private val idGenerator: IdGenerator,
    private val folderTemplateRepository: FolderTemplateRepository,
    private val folderTemplateAndFolderService: FolderTemplateAndFolderService,
) {

    @Transactional(readOnly = true)
    fun findAllFolderTemplates(): List<FolderTemplate> {
        return folderTemplateRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findByIdentifier(folderTemplateIdentifier: String): FolderTemplate {
        return folderTemplateRepository.findByIdentifier(folderTemplateIdentifier)
    }

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
        val folderTemplate =
            folderTemplateRepository.findByIdentifier(identifier)
        folderTemplateAndFolderService.rename(folderTemplate, newName)
        folderTemplateRepository.save(folderTemplate)
    }

    fun addSubFolder(identifier: String, subFolderName: String): String {
        val folderTemplate =
            folderTemplateRepository.findByIdentifier(identifier)
        return folderTemplateAndFolderService.addSubFolderTemplate(folderTemplate, subFolderName)
    }

    fun changeAssociatedDocumentType(identifier: String, newDocumentType: DocumentType) {
        val folderTemplate =
            folderTemplateRepository.findByIdentifier(identifier)
        folderTemplateAndFolderService.changeAssociatedDocumentType(folderTemplate, newDocumentType)
    }

}
