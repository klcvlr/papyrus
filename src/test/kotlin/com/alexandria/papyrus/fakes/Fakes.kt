package com.alexandria.papyrus.fakes

import com.alexandria.papyrus.domain.model.Document
import com.alexandria.papyrus.domain.model.DocumentType
import com.alexandria.papyrus.domain.model.FileWrapper
import com.alexandria.papyrus.domain.model.Folder
import com.alexandria.papyrus.domain.model.FolderTemplate
import io.github.serpro69.kfaker.Faker

val faker = Faker()

fun aDocument(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.gameOfThrones.houses(),
    parentFolder: Folder = aFolder(),
    type: DocumentType? = null,
    predictedType: DocumentType? = null,
    user: String = aUser(),
    fileIdentifier: String = faker.random.nextUUID(),
) = Document(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    type = type,
    predictedType = predictedType,
    user = user,
    fileIdentifier = fileIdentifier,
)

fun aFolder(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.studioGhibli.characters(),
    parentFolder: Folder? = null,
    template: FolderTemplate = aFolderTemplate(),
    associatedDocumentType: DocumentType? = null,
    user: String = aUser(),
) = Folder(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    template = template,
    associatedDocumentType = associatedDocumentType,
    user = user,
)

fun aDocumentType(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.harryPotter.spells(),
    user: String = aUser(),
) = DocumentType(
    identifier = identifier,
    name = name,
    user,
)

fun aFolderTemplate(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.rickAndMorty.characters(),
    parentFolder: FolderTemplate? = null,
    user: String = aUser(),
    associatedDocumentType: DocumentType? = null,
) = FolderTemplate(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    associatedDocumentType = associatedDocumentType,
    user = user,
)

fun aFileWrapper(
    name: String = faker.onePiece.islands(),
    content: ByteArray = faker.futurama.quotes().toByteArray(),
    contentType: String = faker.file.mimeType.application(),
) = FileWrapper(
    name = name,
    content = content,
    contentType = contentType,
)

fun aUser(name: String = faker.name.name()) = name
