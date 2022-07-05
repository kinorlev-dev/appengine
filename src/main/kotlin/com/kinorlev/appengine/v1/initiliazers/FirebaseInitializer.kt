package com.kinorlev.appengine.v1.initiliazers


import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kinorlev.appengine.v1.config.EnvironmentWrapper
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.FileInputStream
import javax.annotation.PostConstruct


/**
 * Must initialize before controllers because some calls use firebase auth to verify token
 */
@Service
@Component
class FirebaseInitializer : ApplicationListener<ApplicationReadyEvent> {

    init {
        println("************** init FirebaseInitializer")
    }


    @Autowired
    lateinit var env: EnvironmentWrapper

    @PostConstruct
    fun initialize() {
        val apps: MutableList<FirebaseApp> = FirebaseApp.getApps()
        if (apps.size > 0) {
            //avoid FirebaseApp [name] already exists error
        } else {
            val cred = FileInputStream(env.getServiceAccountPath())
            val firestoreOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(cred))
                .build()
            val app = FirebaseApp.initializeApp(firestoreOptions)

            println("*************** Firebase application has been initialized **********************")
        }

    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        //println("***************** onApplicationEvent")
    }


}
