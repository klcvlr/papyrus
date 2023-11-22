package com.alexandria.papyrus.infrastructure.api

import com.alexandria.papyrus.domain.model.FolderTemplate
import com.alexandria.papyrus.infrastructure.api.DocumentTypeView.Companion.toDocumentTypeView

data class CreateFolderTemplateRequest(val name: String)

data class CreateSubFolderTemplateRequest(val name: String)

data class RenameFolderTemplateRequest(val name: String)

data class ChangeAssociatedDocumentTypeRequest(val typeIdentifier: String)

data class FolderTemplateView(
    val identifier: String,
    val name: String,
    val associatedDocumentType: String?,
    val parentFolderIdentifier: String?,
) {
    companion object {
        fun toFolderTemplateView(folderTemplate: FolderTemplate): FolderTemplateView {
            return FolderTemplateView(
                identifier = folderTemplate.identifier,
                name = folderTemplate.name,
                associatedDocumentType = folderTemplate.documentType?.identifier,
                parentFolderIdentifier = folderTemplate.parentFolder?.identifier,
            )
        }
    }
}

data class DetailedFolderTemplateView(
    val identifier: String,
    val name: String,
    val associatedDocumentType: DocumentTypeView?,
    val subFolderTemplates: List<DetailedFolderTemplateView> = emptyList(),
    val parentFolderIdentifier: String?,
) {
    companion object {
        fun toDetailedFolderTemplateView(folderTemplate: FolderTemplate): DetailedFolderTemplateView {
            return DetailedFolderTemplateView(
                identifier = folderTemplate.identifier,
                name = folderTemplate.name,
                associatedDocumentType =
                    if (folderTemplate.documentType != null) {
                        toDocumentTypeView(
                            folderTemplate.documentType!!,
                        )
                    } else {
                        null
                    },
                parentFolderIdentifier = folderTemplate.parentFolder?.identifier,
                subFolderTemplates = folderTemplate.subFolders.map { toDetailedFolderTemplateView(it) },
            )
        }
    }
}
