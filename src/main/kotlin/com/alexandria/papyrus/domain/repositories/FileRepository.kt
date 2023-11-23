package com.alexandria.papyrus.domain.repositories

import com.alexandria.papyrus.domain.model.FileWrapper

interface FileRepository {
    fun findByIdentifier(identifier: String): FileWrapper?

    fun save(file: FileWrapper)
}
