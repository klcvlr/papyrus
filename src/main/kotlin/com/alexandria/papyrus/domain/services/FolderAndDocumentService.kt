package com.alexandria.papyrus.domain.services

import com.alexandria.papyrus.domain.DocumentNotFoundException
import com.alexandria.papyrus.domain.FolderNotFoundException
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository

class FolderAndDocumentService(
    private val folderRepository: FolderRepository,
    private val documentRepository: DocumentRepository,
) {
    fun addDocumentToFolder(
        documentIdentifier: String,
        folderIdentifier: String,
    ) {
        val document = documentRepository.findByIdentifier(documentIdentifier) ?: throw DocumentNotFoundException(documentIdentifier)
        val folder = folderRepository.findByIdentifier(folderIdentifier) ?: throw FolderNotFoundException(folderIdentifier)
        document.changeParentFolder(folder)
        folder.addDocument(document)
        folderRepository.save(folder)
    }
}
