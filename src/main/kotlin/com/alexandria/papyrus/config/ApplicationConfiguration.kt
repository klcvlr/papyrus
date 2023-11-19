package com.alexandria.papyrus.config

import com.alexandria.papyrus.application.DocumentUseCases
import com.alexandria.papyrus.application.FolderTemplateUseCases
import com.alexandria.papyrus.application.FolderUseCases
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository
import com.alexandria.papyrus.domain.repositories.FolderTemplateRepository
import com.alexandria.papyrus.domain.services.FolderAndDocumentService
import com.alexandria.papyrus.domain.services.FolderTemplateAndFolderService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {
    @Bean
    fun documentUseCases(
        idGenerator: IdGenerator,
        documentRepository: DocumentRepository,
        folderRepository: FolderRepository,
    ) = DocumentUseCases(
        idGenerator = idGenerator,
        documentRepository = documentRepository,
        folderRepository = folderRepository,
    )

    @Bean
    fun folderTemplateUseCases(
        idGenerator: IdGenerator,
        folderTemplateAndFolderService: FolderTemplateAndFolderService,
        folderTemplateRepository: FolderTemplateRepository,
    ) = FolderTemplateUseCases(
        idGenerator = idGenerator,
        folderTemplateRepository = folderTemplateRepository,
        folderTemplateAndFolderService = folderTemplateAndFolderService,
    )

    @Bean
    fun foldersUseCases(
        folderRepository: FolderRepository,
        folderTemplateRepository: FolderTemplateRepository,
        folderTemplateAndFolderService: FolderTemplateAndFolderService,
    ) = FolderUseCases(
        folderRepository = folderRepository,
        folderTemplateRepository = folderTemplateRepository,
        folderTemplateAndFolderService = folderTemplateAndFolderService,
    )

    @Bean
    fun folderTemplateAndFolderService(
        idGenerator: IdGenerator,
        folderTemplateRepository: FolderTemplateRepository,
        folderRepository: FolderRepository,
    ) = FolderTemplateAndFolderService(
        idGenerator = idGenerator,
        folderRepository = folderRepository,
        folderTemplateRepository = folderTemplateRepository,
    )

    @Bean
    fun folderAndDocumentService(
        folderRepository: FolderRepository,
        documentRepository: DocumentRepository,
    ) = FolderAndDocumentService(
        folderRepository = folderRepository,
        documentRepository = documentRepository,
    )
}
