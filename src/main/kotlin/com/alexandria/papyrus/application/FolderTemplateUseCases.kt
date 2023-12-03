package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.FolderTemplateNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
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
    fun findAllRootFolderTemplatesForUser(userId: String): List<FolderTemplate> {
        return folderTemplateRepository.findAllRootFolderTemplatesForUser(userId)
    }

    @Transactional(readOnly = true)
    fun findByIdentifier(folderTemplateIdentifier: String): FolderTemplate {
        return folderTemplateRepository.findByIdentifier(folderTemplateIdentifier)
            ?: throw FolderTemplateNotFoundException(folderTemplateIdentifier)
    }

    fun create(
        userId: String,
        name: String,
    ): String {
        val folderTemplate =
            FolderTemplate(
                identifier = idGenerator.generate(),
                name = name,
                parentFolder = null,
                associatedDocumentCategory = null,
                user = userId,
            )
        folderTemplateRepository.save(folderTemplate)
        return folderTemplate.identifier
    }

    fun rename(
        identifier: String,
        newName: String,
        userIdentifier: String,
    ) {
        val folderTemplate =
            folderTemplateRepository.findByIdentifier(identifier) ?: throw FolderTemplateNotFoundException(identifier)
        folderTemplateAndFolderService.rename(folderTemplate, newName)
        folderTemplateRepository.save(folderTemplate)
    }

    fun addSubFolder(
        identifier: String,
        subFolderName: String,
        userIdentifier: String,
    ): String {
        val folderTemplate =
            folderTemplateRepository.findByIdentifier(identifier) ?: throw FolderTemplateNotFoundException(identifier)
        return folderTemplateAndFolderService.addSubFolderTemplate(folderTemplate, subFolderName, userIdentifier)
    }

    fun changeAssociatedDocumentCategory(
        folderTemplateIdentifier: String,
        newDocumentCategoryIdentifier: String,
        userIdentifier: String,
    ) {
        folderTemplateAndFolderService.changeAssociatedDocumentCategory(
            folderTemplateIdentifier,
            newDocumentCategoryIdentifier,
            userIdentifier,
        )
    }
}
