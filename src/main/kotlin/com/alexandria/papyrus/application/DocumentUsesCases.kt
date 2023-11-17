package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.Document
import com.alexandria.papyrus.domain.DocumentRepository
import com.alexandria.papyrus.domain.FolderRepository
import com.alexandria.papyrus.domain.exceptions.FolderNotFoundException
import com.alexandria.papyrus.domain.utils.IdGenerator

class DocumentUsesCases(
    private val idGenerator: IdGenerator,
    private val documentRepository: DocumentRepository,
    private val folderRepository: FolderRepository,
) {

    fun createDocument(name: String, parentFolderIdentifier: String): String {
        val parentFolder = getParentFolder(parentFolderIdentifier)
        val document = Document(
            identifier = idGenerator.generate(), name = name, parentFolder = parentFolder
        )
        documentRepository.save(document)
        return document.identifier
    }


    private fun getParentFolder(parentFolderIdentifier: String) =
        folderRepository.findByIdentifier(parentFolderIdentifier) ?: throw FolderNotFoundException(
            parentFolderIdentifier
        )
}