package com.alexandria.papyrus.domain.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class Folder internal constructor() {
    @Id
    @Column(name = "identifier")
    private var _identifier: String = ""

    @Column(name = "name")
    private var _name: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_identifier", referencedColumnName = "identifier")
    private var _parentFolder: Folder? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_folder_identifier", referencedColumnName = "identifier")
    private var _rootFolder: Folder? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associated_document_category_identifier", referencedColumnName = "identifier")
    private var _associatedDocumentCategory: DocumentCategory? = null

    @OneToMany(mappedBy = "_parentFolder", cascade = [CascadeType.ALL])
    private var _subFolders: MutableList<Folder> = mutableListOf()

    @OneToMany(mappedBy = "_parentFolder", cascade = [CascadeType.ALL])
    private var _documents: MutableList<Document> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_identifier", referencedColumnName = "identifier")
    private var _template: FolderTemplate? = null

    @Column(name = "user_identifier")
    private var _user: String = ""

    internal constructor(
        identifier: String,
        template: FolderTemplate,
        name: String,
        parentFolder: Folder? = null,
        associatedDocumentCategory: DocumentCategory? = null,
        user: String,
    ) : this() {
        this._identifier = identifier
        this._template = template
        this._name = name
        this._parentFolder = parentFolder
        this._associatedDocumentCategory = associatedDocumentCategory
        this._rootFolder = parentFolder?._rootFolder ?: this
        this._user = user
    }

    fun rename(newName: String) {
        _name = newName
    }

    fun changeAssociatedDocumentCategory(documentCategory: DocumentCategory) {
        _associatedDocumentCategory = documentCategory
    }

    fun addDocument(document: Document) {
        _documents.add(document)
    }

    fun addSubFolder(folder: Folder) {
        folder._parentFolder = this
        folder._rootFolder = this._rootFolder
        _subFolders.add(folder)
    }

    // ------------------ GETTERS ------------------
    val identifier: String get() = _identifier
    val template: FolderTemplate get() = _template!!
    val parentFolder: Folder? get() = _parentFolder
    val rootFolder: Folder? get() = _rootFolder
    val name: String get() = _name
    val associatedDocumentCategory: DocumentCategory? get() = _associatedDocumentCategory
    val subFolders: List<Folder> get() = _subFolders
    val documents: List<Document> get() = _documents
    val user: String get() = _user

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Folder

        return _identifier == other._identifier
    }

    override fun hashCode(): Int {
        return _identifier.hashCode()
    }
}
