package com.alexandria.papyrus.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
@Profile("swagger-ui-workaround")
class OpenAPIConfiguration {
    /**
     * This bean is used to configure the JSON media type to be application/octet-stream to work around a bug/limitation in Swagger UI.
     * Our application allows posting multipart with parts that have different content-types. Swagger UI does let you specify the content-type.
     * https://github.com/swagger-api/swagger-ui/issues/6462
     */
    @Bean
    fun octetStreamJsonConverter(): MappingJackson2HttpMessageConverter {
        val converter = MappingJackson2HttpMessageConverter()
        converter.supportedMediaTypes =
            listOf(MediaType("application", "octet-stream"))
        return converter
    }
}
