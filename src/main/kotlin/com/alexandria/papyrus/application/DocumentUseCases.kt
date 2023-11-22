package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.DocumentNotFoundException
import com.alexandria.papyrus.domain.DocumentTypeNotFoundException
import com.alexandria.papyrus.domain.FolderNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.model.Document
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.DocumentTypeRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
class DocumentUseCases(
    private val idGenerator: IdGenerator,
    private val documentRepository: DocumentRepository,
    private val documentTypeRepository: DocumentTypeRepository,
    private val folderRepository: FolderRepository,
) {
    @Transactional(readOnly = true)
    fun findByIdentifier(identifier: String): Document {
        return documentRepository.findByIdentifier(identifier) ?: throw DocumentNotFoundException(identifier)
    }

    fun createDocument(
        name: String,
        parentFolderIdentifier: String,
    ): String {
        val parentFolder =
            folderRepository.findByIdentifier(parentFolderIdentifier) ?: throw FolderNotFoundException(
                parentFolderIdentifier,
            )
        val document =
            Document(
                identifier = idGenerator.generate(),
                name = name,
                parentFolder = parentFolder,
            )
        documentRepository.save(document)
        return document.identifier
    }

    fun changeType(
        documentIdentifier: String,
        documentTypeIdentifier: String,
    ) {
        val document = documentRepository.findByIdentifier(documentIdentifier) ?: throw DocumentNotFoundException(documentIdentifier)
        val type = documentTypeRepository.findByIdentifier(documentTypeIdentifier) ?: throw DocumentTypeNotFoundException(documentTypeIdentifier)
        document.changeType(type)
        documentRepository.save(document)
    }

    fun changePredictedType(
        documentIdentifier: String,
        documentTypeIdentifier: String,
    ) {
        val document = documentRepository.findByIdentifier(documentIdentifier) ?: throw DocumentNotFoundException(documentIdentifier)
        val type = documentTypeRepository.findByIdentifier(documentTypeIdentifier) ?: throw DocumentTypeNotFoundException(documentTypeIdentifier)
        document.changePredictedType(type)
        documentRepository.save(document)
    }
}
