package com.alexandria.papyrus.adapters.messaging

import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component

@Component
class SQSMessageHandler {
    // TODO we are just testing that we can read the message we sent to the queue
    // since this works, we can probably rename this class and move it with the exposition packages (along with rest and why not graphql)
    @SqsListener("uploaded-document-queue")
    fun listen(message: String?) {
        println(message)
    }
}
