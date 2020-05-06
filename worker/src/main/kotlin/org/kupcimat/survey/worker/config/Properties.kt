package org.kupcimat.survey.worker.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("survey")
data class SurveyProperties(
    val question: String
)
