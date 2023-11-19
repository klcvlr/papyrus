package com.alexandria.papyrus.domain.services

import com.alexandria.papyrus.domain.model.Document
import com.alexandria.papyrus.domain.model.Folder
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository

class FolderAndDocumentService(
    private val folderRepository: FolderRepository,
    private val documentRepository: DocumentRepository,
) {
    fun addDocumentToFolder(
        document: Document,
        folder: Folder,
    ) {
        document.addToFolder(folder)
        folder.addDocument(document)
        folderRepository.save(folder)
        documentRepository.save(document)
    }
}
