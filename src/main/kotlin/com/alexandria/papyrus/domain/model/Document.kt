package com.alexandria.papyrus.domain.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "document")
class Document internal constructor() {
    @Id
    @Column(name = "identifier")
    private var _identifier: String = ""

    @Column(name = "name")
    private var _name: String = ""

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "parent_folder_identifier", referencedColumnName = "identifier")
    private var _parentFolder: Folder? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "type_identifier", referencedColumnName = "identifier")
    private var _type: DocumentType? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "predicted_type_identifier", referencedColumnName = "identifier")
    private var _predictedType: DocumentType? = null

    internal constructor(
        identifier: String,
        name: String,
        parentFolder: Folder,
        type: DocumentType? = null,
        predictedType: DocumentType? = null,
    ) : this() {
        this._identifier = identifier
        this._name = name
        this._parentFolder = parentFolder
        this._type = type
        this._predictedType = predictedType
    }

    fun rename(newName: String) {
        _name = newName
    }

    fun changeType(newType: DocumentType) {
        _type = newType
    }

    fun changePredictedType(newPredictedType: DocumentType) {
        _predictedType = newPredictedType
    }

    fun addToFolder(folder: Folder) {
        _parentFolder = folder
    }

    // ------------------ GETTERS ------------------
    val identifier: String get() = _identifier
    val parentFolder: Folder get() = _parentFolder!!
    val name: String get() = _name
    val type: DocumentType? get() = _type
    val predictedType: DocumentType? get() = _predictedType

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        return _identifier == other._identifier
    }

    override fun hashCode(): Int {
        return _identifier.hashCode()
    }
}
