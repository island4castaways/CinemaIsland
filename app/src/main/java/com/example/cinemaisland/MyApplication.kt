package com.example.cinemaisland

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class MyApplication: MultiDexApplication() {
    companion object {
        lateinit var auth: FirebaseAuth
        lateinit var db: FirebaseFirestore
        lateinit var storage: FirebaseStorage

        //전화번호 인증
        var verificationId = ""

        //email 인증 사용시
//        var email: String? = null
//
//        fun checkAuth(): Boolean {
//            val currentUser = auth.currentUser
//            return currentUser?.let {
//                email = currentUser.email
//                if(currentUser.isEmailVerified) true
//                else false
//            } ?: let { false }
//        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage
    }
}