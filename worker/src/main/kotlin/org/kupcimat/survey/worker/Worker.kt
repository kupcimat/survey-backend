package org.kupcimat.survey.worker

import kotlinx.coroutines.runBlocking
import org.kupcimat.survey.common.message.Queues
import org.kupcimat.survey.common.model.TaskStatus
import org.kupcimat.survey.common.repository.UserTaskRepository
import org.kupcimat.survey.worker.config.SurveyProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class Worker(
    val surveyProperties: SurveyProperties,
    val userTaskRepository: UserTaskRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(queues = [Queues.SENT_MESSAGES])
    fun sendMessage(taskId: String) = runBlocking<Unit> {
        val task = userTaskRepository.findById(taskId) ?: throw TaskNotFoundException(taskId)
        logger.info("component=worker action=message-sent task-id=$taskId")
        sendSms(task.phone, surveyProperties.question)
        userTaskRepository.save(task.copy(status = TaskStatus.SENT))
    }

    @RabbitListener(queues = [Queues.RECEIVED_MESSAGES])
    fun receiveMessage(taskId: String) = runBlocking<Unit> {
        val task = userTaskRepository.findById(taskId) ?: throw TaskNotFoundException(taskId)
        val taskMessage = task.message ?: throw TaskMessageNotFoundException(taskId)
        logger.info("component=worker action=cloud-function-run task-id=$taskId")
        executeFunction(task.functionId, taskMessage)
        userTaskRepository.save(task.copy(status = TaskStatus.DONE))
    }
}
