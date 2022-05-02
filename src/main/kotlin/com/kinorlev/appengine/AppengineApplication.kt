package com.kinorlev.appengine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = arrayOf(
        UserDetailsServiceAutoConfiguration::class // prevent generated security password:
    )
)
class AppengineApplication

fun main(args: Array<String>) {
    runApplication<AppengineApplication>(*args)
}
