package com.kinorlev.appengine.v1.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment

class EnvironmentWrapper(
    @Autowired
    var env: Environment
) {

    fun getIcountToken(): String = env.getProperty("icountToken")!!

}