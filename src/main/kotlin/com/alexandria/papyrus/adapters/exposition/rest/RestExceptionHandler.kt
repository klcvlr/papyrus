package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.domain.DocumentCategoryNotFoundException
import com.alexandria.papyrus.domain.DocumentNotFoundException
import com.alexandria.papyrus.domain.FileNotFoundException
import com.alexandria.papyrus.domain.FolderNotFoundException
import com.alexandria.papyrus.domain.FolderTemplateNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(
        FolderNotFoundException::class,
        FolderTemplateNotFoundException::class,
        DocumentNotFoundException::class,
        DocumentCategoryNotFoundException::class,
        FileNotFoundException::class,
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleAccessDeniedException(exception: Exception) = exception.message
}
