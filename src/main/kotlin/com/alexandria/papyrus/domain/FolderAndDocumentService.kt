package com.alexandria.papyrus.domain

class FolderAndDocumentService(
    private val folderRepository: FolderRepository,
    private val documentRepository: DocumentRepository,
) {
    fun addDocumentToFolder(document: Document, folder: Folder) {
        document.addToFolder(folder)
        folder.addDocument(document)
        folderRepository.save(folder)
        documentRepository.save(document)
    }

}