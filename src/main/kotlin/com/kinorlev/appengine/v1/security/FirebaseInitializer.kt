package com.kinorlev.appengine.v1.security

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.FileInputStream
import javax.annotation.PostConstruct


@Service
@Component
class FirebaseInitializer : ApplicationListener<ApplicationReadyEvent> {

    init {
        println("************** init FirebaseInitializer")
    }


    @Autowired
    lateinit var env: Environment


    /**
     * for safety the path of the file that contains the Credentials is obtained from enviroment variable
     * named: GOOGLE_APPLICATION_CREDENTIALS
     * in ubuntu for setting permanently environment variable i used this steps:
     * 1) sudo -H gedit /etc/environment
     * 2) GOOGLE_APPLICATION_CREDENTIALS="absolute/path/to/file.json"//no white spaces! dont forget semi-colon
     * 3) SAVE FILE!
     * 4) LOG OUT!
     * 5) you can test the environment variables simply by printing with java program (System.getenv())
     */
    @PostConstruct
    fun initialize() {
        try {
            val profile = env.activeProfiles[0]
            println("profile: $profile")
            val serviceAccount = when (profile) {
                "dev", "prod" -> {
                    val serviceaccount = env.get("serviceaccount")
                    val fis = FileInputStream(serviceaccount)
                    val inputStream = IOUtils.toBufferedInputStream(fis)
                    GoogleCredentials.fromStream(inputStream)
                }
                else -> GoogleCredentials.getApplicationDefault()
            }


            val options: FirebaseOptions = FirebaseOptions.builder()
                .setCredentials(serviceAccount)
                .build()

            FirebaseApp.initializeApp(options)
            println("*************** Firebase application has been initialized **********************")
        } catch (e: Exception) {
            println("Firebase application has FAILED to initialized")
            println(e.message)
        }

    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        //println("***************** onApplicationEvent")
    }


}
