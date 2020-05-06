package org.kupcimat.survey.integration

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration

/**
 * Base class for all integration tests.
 */
open class SetupIT {

    private val webClientTimeout = Duration.ofSeconds(30)
    private val serverUrl: String = System.getProperty("serverUrl", "http://localhost:8080")

    val webTestClient: WebTestClient = WebTestClient.bindToServer()
        .baseUrl(serverUrl)
        .defaultHeaders { it.accept = listOf(APPLICATION_JSON) }
        .responseTimeout(webClientTimeout)
        .build()

    fun readFile(filename: String): String {
        return this::class.java
            .getResource("/$filename")
            .readText(Charsets.UTF_8)
    }

    // TODO add timeout
    fun <T> poll(f: () -> T, condition: (T) -> Boolean, delaySeconds: Int = 2) {
        var result = f()
        while (!condition(result)) {
            Thread.sleep(delaySeconds * 1000L)
            result = f()
        }
    }
}
