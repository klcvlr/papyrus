package com.alexandria.papyrus.config

import com.alexandria.papyrus.application.DocumentTypeUseCases
import com.alexandria.papyrus.application.DocumentUseCases
import com.alexandria.papyrus.application.FolderTemplateUseCases
import com.alexandria.papyrus.application.FolderUseCases
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.DocumentTypeRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository
import com.alexandria.papyrus.domain.repositories.FolderTemplateRepository
import com.alexandria.papyrus.domain.services.FolderTemplateAndFolderService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {
    @Bean
    fun idGenerator() = IdGenerator()

    @Bean
    fun documentUseCases(
        idGenerator: IdGenerator,
        documentRepository: DocumentRepository,
        folderRepository: FolderRepository,
        documentTypeRepository: DocumentTypeRepository,
    ) = DocumentUseCases(
        idGenerator = idGenerator,
        documentRepository = documentRepository,
        folderRepository = folderRepository,
        documentTypeRepository = documentTypeRepository,
    )

    @Bean
    fun documentTypeUseCase(
        idGenerator: IdGenerator,
        documentTypeRepository: DocumentTypeRepository,
    ) = DocumentTypeUseCases(
        idGenerator = idGenerator,
        documentTypeRepository = documentTypeRepository,
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
        documentTypeRepository: DocumentTypeRepository,
    ) = FolderTemplateAndFolderService(
        idGenerator = idGenerator,
        folderRepository = folderRepository,
        folderTemplateRepository = folderTemplateRepository,
        documentTypeRepository = documentTypeRepository,
    )
}
