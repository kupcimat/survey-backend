package org.kupcimat.survey.restapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class RestapiApp

fun main(args: Array<String>) {
    runApplication<RestapiApp>(*args)
}
