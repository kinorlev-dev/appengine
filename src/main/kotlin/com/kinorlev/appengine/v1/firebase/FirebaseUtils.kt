package com.kinorlev.appengine.v1.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.firestore.WriteResult
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.kinorlev.appengine.v1.config.EnvironmentWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import java.io.FileInputStream
import java.nio.file.Paths

class FirebaseUtils constructor(
    @Autowired var env: EnvironmentWrapper
) {

    private val fireStore: Firestore by lazy {
        initializeFirebase()
        getFirestore()
    }

    val fireStoreRef: FireStoreRef by lazy {
        FireStoreRef(fireStore)
    }


    fun getFirebaseToken(idToken: String): FirebaseToken? {
        return try {
            val fbToken: FirebaseToken = FirebaseAuth.getInstance().verifyIdToken(idToken)
            fbToken
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFirestore(): Firestore {
        val firestoreOptions = FirestoreOptions.newBuilder()
            .setCredentials(GoogleCredentials.fromStream(getServiceAccount()))
            .build()

        val firestore: Firestore = firestoreOptions.service
        val message = if (env.isDevelop()) {
            val projectId = firestoreOptions.projectId
            "firestore projectId: $projectId 😍"
        } else {
            "not develop 😱"
        }
        println(message)
        return firestore
    }

    private fun getServiceAccount(): FileInputStream {
        val path = env.getServiceAccountPath()
        return FileInputStream(path)
    }


    private fun initializeFirebase() {

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

}