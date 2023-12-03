package com.alexandria.papyrus.config

import com.alexandria.papyrus.application.DocumentCategoryUseCases
import com.alexandria.papyrus.application.DocumentUseCases
import com.alexandria.papyrus.application.FolderTemplateUseCases
import com.alexandria.papyrus.application.FolderUseCases
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.notification.NotificationPublisher
import com.alexandria.papyrus.domain.repositories.DocumentCategoryRepository
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.FileRepository
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
        documentCategoryRepository: DocumentCategoryRepository,
        fileRepository: FileRepository,
        notificationPublisher: NotificationPublisher,
    ) = DocumentUseCases(
        idGenerator = idGenerator,
        documentRepository = documentRepository,
        folderRepository = folderRepository,
        documentCategoryRepository = documentCategoryRepository,
        fileRepository = fileRepository,
        notificationPublisher = notificationPublisher,
    )

    @Bean
    fun documentCategoryUseCase(
        idGenerator: IdGenerator,
        documentCategoryRepository: DocumentCategoryRepository,
    ) = DocumentCategoryUseCases(
        idGenerator = idGenerator,
        documentCategoryRepository = documentCategoryRepository,
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
        documentCategoryRepository: DocumentCategoryRepository,
    ) = FolderTemplateAndFolderService(
        idGenerator = idGenerator,
        folderRepository = folderRepository,
        folderTemplateRepository = folderTemplateRepository,
        documentCategoryRepository = documentCategoryRepository,
    )
}
