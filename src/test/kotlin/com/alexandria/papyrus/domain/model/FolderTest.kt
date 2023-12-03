package com.alexandria.papyrus.domain.model

import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aDocumentCategory
import com.alexandria.papyrus.fakes.aFolder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FolderTest {
    @Test
    fun `a folder can be renamed`() {
        val folder = aFolder(name = "oldName")

        folder.rename("newName")

        assertThat(folder.name).isEqualTo("newName")
    }

    @Test
    fun `a folder's associated document category can be changed`() {
        val oldDocumentCategory = aDocumentCategory(identifier = "oldDocumentCategoryIdentifier")
        val folder = aFolder(associatedDocumentCategory = oldDocumentCategory)

        val newDocumentCategory = aDocumentCategory(identifier = "newDocumentCategoryIdentifier")
        folder.changeAssociatedDocumentCategory(newDocumentCategory)

        assertThat(folder.associatedDocumentCategory).isEqualTo(newDocumentCategory)
    }

    @Test
    fun `a folder can be added to another folder`() {
        val folder = aFolder()
        val subFolder = aFolder()

        folder.addSubFolder(subFolder)

        assertThat(subFolder.parentFolder).isEqualTo(folder)
        assertThat(folder.subFolders).contains(subFolder)
    }

    @Test
    fun `a document can be added to a folder`() {
        val folder = aFolder()
        val document = aDocument()

        folder.addDocument(document)

        assertThat(folder.documents).contains(document)
    }
}
