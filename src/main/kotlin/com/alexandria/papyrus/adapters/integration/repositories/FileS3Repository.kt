package com.alexandria.papyrus.adapters.integration.repositories

import com.alexandria.papyrus.domain.model.FileWrapper
import com.alexandria.papyrus.domain.repositories.FileRepository
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Component
class FileS3Repository(private val s3Client: S3Client) : FileRepository {
    override fun findByIdentifier(identifier: String): FileWrapper? {
        TODO()
    }

    override fun save(file: FileWrapper) {
        val putObjectRequest =
            PutObjectRequest.builder()
                .bucket("io.alexandria.papyrus.codex")
                .key(file.name)
                .build()

        val fileContent = RequestBody.fromBytes(file.content)

        s3Client.putObject(
            putObjectRequest,
            fileContent,
        )
    }
}
