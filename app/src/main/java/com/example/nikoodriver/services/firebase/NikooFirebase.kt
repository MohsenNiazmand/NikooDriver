package com.example.nikoodriver.services.firebase

import com.google.firebase.messaging.FirebaseMessagingService

class NikooFirebase : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}