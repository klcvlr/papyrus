package com.alexandria.papyrus.domain.model

import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aDocumentType
import com.alexandria.papyrus.fakes.aFolder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FolderTest {
    @Test
    fun `a folder can be renamed`() {
        val folder = aFolder(name = "oldName")

        folder.rename("newName")

        Assertions.assertThat(folder.name).isEqualTo("newName")
    }

    @Test
    fun `a folder's associated document type can be changed`() {
        val oldDocumentType = aDocumentType(identifier = "oldDocumentTypeIdentifier")
        val folder = aFolder(associatedDocumentType = oldDocumentType)

        val newDocumentType = aDocumentType(identifier = "newDocumentTypeIdentifier")
        folder.changeAssociatedDocumentType(newDocumentType)

        Assertions.assertThat(folder.associatedDocumentType).isEqualTo(newDocumentType)
    }

    @Test
    fun `a folder can be added to another folder`() {
        val folder = aFolder()
        val subFolder = aFolder()

        folder.addSubFolder(subFolder)

        Assertions.assertThat(subFolder.parentFolder).isEqualTo(folder)
        Assertions.assertThat(folder.subFolders).contains(subFolder)
    }

    @Test
    fun `a document can be added to a folder`() {
        val folder = aFolder()
        val document = aDocument()

        folder.addDocument(document)

        Assertions.assertThat(folder.documents).contains(document)
    }
}
