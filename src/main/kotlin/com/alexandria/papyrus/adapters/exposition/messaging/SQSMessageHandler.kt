package com.alexandria.papyrus.adapters.exposition.messaging

import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component

@Component
class SQSMessageHandler {
    @SqsListener("\${papyrus.cloud.queue}")
    fun listen(message: String?) {
        println(message)
    }
}
