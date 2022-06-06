package com.kinorlev.appengine.v1.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment

class EnvironmentWrapper(
    @Autowired
    private var env: Environment
) {

    fun getServiceAccountPath(): String = env.getProperty("serviceaccount")!!

    fun getProfile() = env.activeProfiles[0]!!

    fun isDevelop() = getProfile() == "dev"

}