package org.kupcimat.survey.restapi.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories(basePackages = ["org.kupcimat.survey.common.repository"])
class MongoConfig
