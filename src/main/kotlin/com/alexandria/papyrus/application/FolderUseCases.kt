package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.FolderNotFoundException
import com.alexandria.papyrus.domain.FolderTemplateNotFoundException
import com.alexandria.papyrus.domain.model.Folder
import com.alexandria.papyrus.domain.repositories.FolderRepository
import com.alexandria.papyrus.domain.repositories.FolderTemplateRepository
import com.alexandria.papyrus.domain.services.FolderTemplateAndFolderService
import org.springframework.transaction.annotation.Transactional

@Transactional
class FolderUseCases(
    private val folderRepository: FolderRepository,
    private val folderTemplateRepository: FolderTemplateRepository,
    private val folderTemplateAndFolderService: FolderTemplateAndFolderService,
) {
    @Transactional(readOnly = true)
    fun findAllFolders(): List<Folder> {
        return folderRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findByIdentifier(
        folderIdentifier: String,
        user: String,
    ): Folder {
        return folderRepository.findByIdentifier(folderIdentifier) ?: throw FolderNotFoundException(
            folderIdentifier,
        )
    }

    fun createFromTemplate(
        folderTemplateIdentifier: String,
        user: String,
    ): String {
        val folderTemplate =
            folderTemplateRepository.findByIdentifier(folderTemplateIdentifier) ?: throw FolderTemplateNotFoundException(
                folderTemplateIdentifier,
            )
        val folder = folderTemplateAndFolderService.createFolderFromTemplate(folderTemplate, user)
        folderRepository.save(folder)
        return folder.identifier
    }
}
