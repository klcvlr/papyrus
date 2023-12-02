package com.alexandria.papyrus.domain.model

import com.alexandria.papyrus.fakes.aDocumentType
import com.alexandria.papyrus.fakes.aFolderTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FolderTemplateTest {
    @Test
    fun `a folderTemplate can be renamed`() {
        val folderTemplate = aFolderTemplate(name = "oldName")

        folderTemplate.rename("newName")

        assertThat(folderTemplate.name).isEqualTo("newName")
    }

    @Test
    fun `a folderTemplate's associated document type can be changed`() {
        val oldDocumentType = aDocumentType(identifier = "oldDocumentTypeIdentifier")
        val folderTemplate = aFolderTemplate(associatedDocumentType = oldDocumentType)

        val newDocumentType = aDocumentType(identifier = "newDocumentTypeIdentifier")
        folderTemplate.changeAssociatedDocumentType(newDocumentType)

        assertThat(folderTemplate.associatedDocumentType).isEqualTo(newDocumentType)
    }

    @Test
    fun `a folderTemplate can be added to another folderTemplate`() {
        val folderTemplate = aFolderTemplate()
        val subFolderTemplate = aFolderTemplate()

        folderTemplate.addSubFolder(subFolderTemplate)

        assertThat(folderTemplate.subFolders).contains(subFolderTemplate)
        assertThat(subFolderTemplate.parentFolder).isEqualTo(folderTemplate)
    }
}
