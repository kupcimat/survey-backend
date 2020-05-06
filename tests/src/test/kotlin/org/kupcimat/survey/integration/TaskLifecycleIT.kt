package org.kupcimat.survey.integration

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.expectBody

class TaskLifecycleIT : SetupIT() {

    @Test
    fun `basic lifecycle test`() {
        // create new task
        val createdTask = webTestClient.post().uri("/api/userTasks")
            .contentType(APPLICATION_JSON)
            .bodyValue(readFile("userTask.json"))
            .exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(APPLICATION_JSON)
            .expectHeader().valueMatches("Location", "/api/userTasks/[0-9a-f]+")
            .expectBody<String>().returnResult()

        val taskUri = createdTask.responseHeaders.location.toString()

        // wait until the message is sent
        poll({ getTaskJson(taskUri) }, { it.contains("SENT") })

        // simulate message response
        webTestClient.post().uri("$taskUri/message")
            .contentType(APPLICATION_JSON)
            .bodyValue(readFile("userTaskMessage.json"))
            .exchange()
            .expectStatus().isAccepted
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody<String>().returnResult()

        // wait until the cloud function is run
        poll({ getTaskJson(taskUri) }, { it.contains("DONE") })

        // TODO check json responses using https://github.com/lukas-krecan/JsonUnit
    }

    fun getTaskJson(taskUri: String): String {
        return webTestClient.get().uri(taskUri)
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .returnResult().responseBody ?: throw AssertionError("Missing body")
    }
}
