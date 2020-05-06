package org.kupcimat.survey.worker

class TaskNotFoundException(taskId: String) :
    RuntimeException("Task $taskId does not exist")

class TaskMessageNotFoundException(taskId: String) :
    RuntimeException("Task $taskId does not have message")
