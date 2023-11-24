package com.alexandria.papyrus.adapters.integration.repositories

import com.alexandria.papyrus.config.CloudEndpoints
import com.alexandria.papyrus.domain.model.FileWrapper
import com.alexandria.papyrus.domain.repositories.FileRepository
import io.awspring.cloud.s3.S3Template
import org.springframework.stereotype.Repository

@Repository
class FileS3Repository(
    private val s3Template: S3Template,
    private val cloudEndpoints: CloudEndpoints,
) :
    FileRepository {
    override fun findByIdentifier(identifier: String): FileWrapper? {
        TODO()
    }

    override fun save(file: FileWrapper) {
        s3Template.upload(cloudEndpoints.bucket, file.name, file.content.inputStream())
    }
}
