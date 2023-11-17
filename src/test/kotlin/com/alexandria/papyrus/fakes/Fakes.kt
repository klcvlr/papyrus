package com.alexandria.papyrus.fakes

import com.alexandria.papyrus.domain.Document
import com.alexandria.papyrus.domain.DocumentType
import com.alexandria.papyrus.domain.Folder
import com.alexandria.papyrus.domain.FolderTemplate
import io.github.serpro69.kfaker.Faker

val faker = Faker()

fun aDocument(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.gameOfThrones.houses(),
    parentFolder: Folder? = null,
    type: DocumentType? = null,
    predictedType: DocumentType? = null
) = Document(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    type = type,
    predictedType = predictedType
)

fun aFolder(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.studioGhibli.characters(),
    parentFolder: Folder? = null,
    templateIdentifier: String = faker.random.nextUUID(),
    associatedDocumentType: DocumentType? = null
) = Folder(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    templateIdentifier = templateIdentifier,
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
    name: String = faker.rickAndMorty.locations(),
    parentFolder: FolderTemplate? = null,
    associatedDocumentType: DocumentType? = null
) = FolderTemplate(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    associatedDocumentType = associatedDocumentType,
)