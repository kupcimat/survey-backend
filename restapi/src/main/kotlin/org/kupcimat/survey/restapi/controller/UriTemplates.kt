package org.kupcimat.survey.restapi.controller

object UriTemplates {

    private const val API_PREFIX = "/api"

    const val USER_TASKS = "$API_PREFIX/userTasks"
    const val USER_TASK = "$USER_TASKS/{taskId}"
}
