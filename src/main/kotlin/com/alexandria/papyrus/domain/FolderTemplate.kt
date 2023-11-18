package com.alexandria.papyrus.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class FolderTemplate() {
    @Id
    @Column(name = "identifier")
    private var _identifier: String = ""

    @Column(name = "name")
    private var _name: String = ""

    @ManyToOne
    @JoinColumn(name = "parent_folder", referencedColumnName = "identifier")
    private var _parentFolder: FolderTemplate? = null

    @OneToMany(mappedBy = "_parentFolder", cascade = [CascadeType.ALL])
    private var _subFolders: List<FolderTemplate> = mutableListOf()

    @ManyToOne
    @JoinColumn(name = "associated_document_type", referencedColumnName = "identifier")
    private var _associatedDocumentType: DocumentType? = null

    constructor(
        identifier: String,
        name: String,
        parentFolder: FolderTemplate? = null,
        associatedDocumentType: DocumentType? = null
    ) : this() {
        this._identifier = identifier
        this._name = name
        this._parentFolder = parentFolder
        this._associatedDocumentType = associatedDocumentType
    }

    fun addSubFolder(subFolder: FolderTemplate) {
        subFolder._parentFolder = this
        this._subFolders = this.subFolders.plus(subFolder)
    }

    fun changeAssociatedDocumentType(documentType: DocumentType) {
        _associatedDocumentType = documentType
    }

    // ------------------ GETTERS ------------------
    val identifier: String get() = _identifier
    val parentFolder: FolderTemplate? get() = _parentFolder
    val name: String get() = _name
    val documentType: DocumentType? get() = _associatedDocumentType
    val subFolders: List<FolderTemplate> get() = _subFolders

    fun rename(newName: String) {
        _name = newName
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FolderTemplate

        return _identifier == other._identifier
    }

    override fun hashCode(): Int {
        return _identifier.hashCode()
    }

    override fun toString(): String {
        return "FolderTemplate(_identifier='$_identifier', _name='$_name', _parentFolder=${_parentFolder?.identifier}, _subFolders=$_subFolders, _associatedDocumentType=$_associatedDocumentType)"
    }
}
