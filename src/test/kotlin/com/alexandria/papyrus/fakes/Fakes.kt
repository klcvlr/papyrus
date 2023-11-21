package com.alexandria.papyrus.fakes

import com.alexandria.papyrus.domain.model.Document
import com.alexandria.papyrus.domain.model.DocumentType
import com.alexandria.papyrus.domain.model.Folder
import com.alexandria.papyrus.domain.model.FolderTemplate
import io.github.serpro69.kfaker.Faker

val faker = Faker()

fun aDocument(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.gameOfThrones.houses(),
    parentFolder: Folder = aFolder(),
    rootFolder: Folder = aFolder(),
    type: DocumentType? = null,
    predictedType: DocumentType? = null,
) = Document(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    rootFolder = rootFolder,
    type = type,
    predictedType = predictedType,
)

fun aFolder(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.studioGhibli.characters(),
    parentFolder: Folder? = null,
    template: FolderTemplate = aFolderTemplate(),
    associatedDocumentType: DocumentType? = null,
) = Folder(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    template = template,
    associatedDocumentType = associatedDocumentType,
)

fun aDocumentType(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.harryPotter.spells(),
) = DocumentType(
    identifier = identifier,
    name = name,
)

fun aFolderTemplate(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.rickAndMorty.characters(),
    parentFolder: FolderTemplate? = null,
    associatedDocumentType: DocumentType? = null,
) = FolderTemplate(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    associatedDocumentType = associatedDocumentType,
)
