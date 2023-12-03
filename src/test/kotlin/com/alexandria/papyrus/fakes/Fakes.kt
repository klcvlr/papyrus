package com.alexandria.papyrus.fakes

import com.alexandria.papyrus.domain.model.Document
import com.alexandria.papyrus.domain.model.DocumentCategory
import com.alexandria.papyrus.domain.model.FileWrapper
import com.alexandria.papyrus.domain.model.Folder
import com.alexandria.papyrus.domain.model.FolderTemplate
import io.github.serpro69.kfaker.Faker

val faker = Faker()

fun aDocument(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.gameOfThrones.houses(),
    parentFolder: Folder = aFolder(),
    category: DocumentCategory? = null,
    predictedCategory: DocumentCategory? = null,
    user: String = aUser(),
    fileIdentifier: String = faker.random.nextUUID(),
) = Document(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    category = category,
    predictedCategory = predictedCategory,
    user = user,
    fileIdentifier = fileIdentifier,
)

fun aFolder(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.studioGhibli.characters(),
    parentFolder: Folder? = null,
    template: FolderTemplate = aFolderTemplate(),
    associatedDocumentCategory: DocumentCategory? = null,
    user: String = aUser(),
) = Folder(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    template = template,
    associatedDocumentCategory = associatedDocumentCategory,
    user = user,
)

fun aDocumentCategory(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.harryPotter.spells(),
    user: String = aUser(),
) = DocumentCategory(
    identifier = identifier,
    name = name,
    user,
)

fun aFolderTemplate(
    identifier: String = faker.random.nextUUID(),
    name: String = faker.rickAndMorty.characters(),
    parentFolder: FolderTemplate? = null,
    user: String = aUser(),
    associatedDocumentCategory: DocumentCategory? = null,
) = FolderTemplate(
    identifier = identifier,
    name = name,
    parentFolder = parentFolder,
    associatedDocumentCategory = associatedDocumentCategory,
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
