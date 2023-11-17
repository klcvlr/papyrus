package com.alexandria.papyrus.domain

class Document(
    identifier: String,
    name: String,
    parentFolder: Folder? = null,
    type: DocumentType? = null,
    predictedType: DocumentType? = null
) {
    private val _identifier: String = identifier
    private var _name: String = name
    private var _parentFolder: Folder? = parentFolder
    private var _type: DocumentType? = type
    private var _predictedType: DocumentType? = predictedType


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
    val parentFolder: Folder? get() = _parentFolder
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

    override fun toString(): String {
        return "Document(_identifier='$_identifier', _name='$_name', _parentFolder=$_parentFolder, _type=$_type, _predictedType=$_predictedType)"
    }

}