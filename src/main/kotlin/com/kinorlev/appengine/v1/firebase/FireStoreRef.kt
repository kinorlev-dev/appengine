package com.kinorlev.appengine.v1.firebase

import com.google.cloud.firestore.Firestore
import org.springframework.core.convert.TypeDescriptor.collection

class FireStoreRef(private val firestore: Firestore) {

    companion object{
        const val SESSION_BRIEF_PREFIX = "session_"
        const val DOC_USER_DETAILS = "user_details"
        const val SESSION_BPMS = "bpms"
        const val SESSION_COHERENCE = "coherence"
    }


    fun root() = firestore

    fun appData() = root().collection("App_data")
    fun macList() = root().collection("MAC_list")
    fun idList() = root().collection("ID_list")
    private fun v2() = root().collection("v2")
    fun ping() = v2().document("ping")
    fun songs() = v2().document("songs")
    fun users() = v2().document("users")
    fun customers() = users().collection("customers")
    fun therapistConnection() = users().collection("therapist_connection")
    fun wannaBeTherapist() = users().collection("wanna_be_therapist")
    fun songsMetaData() = songs().collection("meta_data")
    fun singleSongsMetaData(songId: String) = songsMetaData().document(songId)
    fun medicalData() = v2().document("medical_data")
    fun userMedicalData(medicalId: String) = medicalData().collection(medicalId)
    fun userSessionsDetails(medicalId: String) = medicalData().collection(medicalId).document("user_details")
    fun userSession(medicalId: String, sessionTs: String) = userSessionsDetails(medicalId).collection(sessionTs)
    fun userSessionBpms(medicalId: String, sessionTs: String) = userSession(medicalId, sessionTs).document("bpms")
    fun userSessionCoherence(medicalId: String, sessionTs: String) =
        userSession(medicalId, sessionTs).document("coherence")


    fun singleMacAddressInstance(macAddress: String) = macList().document(macAddress)
    fun singleSensorId(sensorId: String) = idList().document(sensorId)
    fun singleCustomer(uid: String) = customers().document(uid)
    fun singleTherapistConnection(uid: String) = therapistConnection().document(uid)
    fun singleSessionBrief(medicalId: String, sessionId: String) = userMedicalData(medicalId).document(sessionId)
    fun initParams() = appData().document("Components for initial operation")

    fun userTherapists(userId: String) = singleCustomer(userId).collection("therapists")
    fun therapistPatients(therapistUid: String) = singleCustomer(therapistUid).collection("patients")

    fun singlePatientOfTherapist(therapistUid: String, patientUid: String) =
        therapistPatients(therapistUid).document(patientUid)

    fun singleTherapistOfPatient(userUid: String, therapistUid: String) = userTherapists(userUid).document(therapistUid)
    fun singleWannaBeTherapist(therapistUid: String) = wannaBeTherapist().document(therapistUid)

}