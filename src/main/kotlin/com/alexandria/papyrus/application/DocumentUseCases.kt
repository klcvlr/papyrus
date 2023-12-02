package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.DocumentNotFoundException
import com.alexandria.papyrus.domain.DocumentTypeNotFoundException
import com.alexandria.papyrus.domain.FileNotFoundException
import com.alexandria.papyrus.domain.FolderNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.model.Document
import com.alexandria.papyrus.domain.model.FileWrapper
import com.alexandria.papyrus.domain.notification.NotificationPublisher
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.DocumentTypeRepository
import com.alexandria.papyrus.domain.repositories.FileRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
class DocumentUseCases(
    private val idGenerator: IdGenerator,
    private val documentRepository: DocumentRepository,
    private val documentTypeRepository: DocumentTypeRepository,
    private val folderRepository: FolderRepository,
    private val fileRepository: FileRepository,
    private val notificationPublisher: NotificationPublisher,
) {
    @Transactional(readOnly = true)
    fun findByIdentifier(identifier: String): Document =
        documentRepository.findByIdentifier(identifier) ?: throw DocumentNotFoundException(identifier)

    @Transactional(readOnly = true)
    fun downloadDocumentByIdentifier(identifier: String): FileWrapper {
        val document = documentRepository.findByIdentifier(identifier) ?: throw DocumentNotFoundException(identifier)
        val file = fileRepository.findByIdentifier(document.fileIdentifier) ?: throw FileNotFoundException(identifier)
        return FileWrapper(document.name, file.content, file.contentType)
    }

    // TODO decide how to handle content-type. Current method based on file name is lightweight but not reliable and forces the user to provide a file name
    // we could infer the content-type with a library like Apache's Tika at some compute cost. But that probably won't the bottleneck when uploading a large number of documents
    fun createDocument(
        parentFolderIdentifier: String,
        file: FileWrapper,
        user: String,
    ): String {
        val parentFolder = folderRepository.findByIdentifier(parentFolderIdentifier) ?: throw FolderNotFoundException(parentFolderIdentifier)
        val fileIdentifier = generateFileId(file)
        fileRepository.save(fileIdentifier, file)
        val document =
            Document(
                identifier = idGenerator.generate(),
                name = file.name,
                parentFolder = parentFolder,
                user = user,
                fileIdentifier = fileIdentifier,
            )
        documentRepository.save(document)
        notificationPublisher.sendUploadNotification(document.identifier)
        return document.identifier
    }

    fun changeType(
        documentIdentifier: String,
        documentTypeIdentifier: String,
        user: String,
    ) {
        val document = documentRepository.findByIdentifier(documentIdentifier) ?: throw DocumentNotFoundException(documentIdentifier)
        val type = documentTypeRepository.findByIdentifier(documentTypeIdentifier) ?: throw DocumentTypeNotFoundException(documentTypeIdentifier)
        document.changeType(type)
        documentRepository.save(document)
    }

    fun changePredictedType(
        documentIdentifier: String,
        documentTypeIdentifier: String,
        user: String,
    ) {
        val document = documentRepository.findByIdentifier(documentIdentifier) ?: throw DocumentNotFoundException(documentIdentifier)
        val type = documentTypeRepository.findByIdentifier(documentTypeIdentifier) ?: throw DocumentTypeNotFoundException(documentTypeIdentifier)
        document.changePredictedType(type)
        documentRepository.save(document)
    }

    private fun generateFileId(file: FileWrapper): String = "${idGenerator.generate()}-${file.name}"

    fun changeStatus(
        documentIdentifier: String,
        status: String,
    ) {
        val document = documentRepository.findByIdentifier(documentIdentifier) ?: throw DocumentNotFoundException(documentIdentifier)
        document.changeStatus(status)
        documentRepository.save(document)
    }

    fun rename(
        documentIdentifier: String,
        newName: String,
        user: String,
    ) {
        val document = documentRepository.findByIdentifier(documentIdentifier) ?: throw DocumentNotFoundException(documentIdentifier)
        document.rename(newName)
        documentRepository.save(document)
    }
}
