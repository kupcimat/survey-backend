package org.kupcimat.survey.worker.config

import org.kupcimat.survey.common.message.Queues
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessagingConfig {

    @Bean
    fun sentMessagesQueue(): Queue {
        return Queue(Queues.SENT_MESSAGES)
    }
}
