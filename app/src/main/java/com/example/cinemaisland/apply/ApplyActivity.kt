package com.example.cinemaisland.apply

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.example.cinemaisland.BaseActivity
import com.example.cinemaisland.MyApplication
import com.example.cinemaisland.MyApplication.Companion.auth
import com.example.cinemaisland.MyApplication.Companion.db
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityApplyBinding
import com.example.cinemaisland.model.Applicant
import com.example.cinemaisland.model.MovieItem
import com.example.cinemaisland.util.dateTimeToString
import com.example.cinemaisland.util.stringToDate
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FieldValue
import java.util.concurrent.TimeUnit

class ApplyActivity : BaseActivity() {
    lateinit var binding: ActivityApplyBinding
    lateinit var name: String
    lateinit var phone: String
    lateinit var email: String
    lateinit var birthDate: String

    var mode = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplyBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

        val movieItem = intent.getSerializableExtra("movieItem") as? MovieItem
        binding.movieTitle.text = movieItem!!.title
        binding.movieSchedule.text = dateTimeToString(movieItem.schedule!!)

        /*
        firebase 인증 test
        phone : +1 650 555 6789
        auth : 123456
         */

        //sendBtn 클릭시
        binding.sendBtn.setOnClickListener {
            Log.d("ssum", "sendBtn clicked")
            binding.auth.visibility = View.VISIBLE

            phone = binding.inputPhone.text.toString()

            val callbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d("ssum", "Verification completed")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d("ssum", "Verification failed")
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    MyApplication.verificationId = p0
                }
            }

            val optionsCompat = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(optionsCompat)
            auth.setLanguageCode("kr")
        }

        //authBtn 클릭시
        binding.authBtn.setOnClickListener {
            val inputAuth = binding.inputAuth.text.toString()
            val credential = PhoneAuthProvider.getCredential(MyApplication.verificationId, inputAuth)
            signInWithPhoneAuthCredential(credential)
        }

        //submitBtn 클릭시
        binding.submitBtn.setOnClickListener {
            name = binding.inputName.text.toString()
            email = binding.inputEmail.text.toString()
            birthDate = binding.inputBirthDate.text.toString()

            if(name.isNullOrBlank()) {
                Log.d("ssum", "inputName 입력 오류")
                Toast.makeText(this, "이름을 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else if(email.isNullOrBlank()) {
                Log.d("ssum", "inputEmail 입력 오류")
                Toast.makeText(this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else if(birthDate.isNullOrBlank()) {
                Log.d("ssum", "inputBirthDate 입력 오류")
                Toast.makeText(this, "생년월일을 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                //DB에 검색
                val applicant = Applicant()
                applicant.name = name
                applicant.phone = phone
                applicant.email = email
                applicant.birthDate = stringToDate(birthDate)
                applicant.id = "$name$phone"
                val documentRef = db.collection("applicant").document("$name$phone")
                var searched: Boolean = false

                db.collection("applicant").get()
                    .addOnCompleteListener { task ->
                        for(document in task.result) {
                            if(document.id == "$name$phone") {
                                Log.d("ssum", "응모자 검색 결과, $name$phone")
                                searched = true

                                //검색된 응모자의 movies에 현재 movie.title 존재하는지 확인
                                val array = document["applied"] as ArrayList<String>
                                if(array.contains("${movieItem.title}")) {
                                    Toast.makeText(this, "이미 응모된 시사회입니다.", Toast.LENGTH_SHORT).show()
                                } else {
                                    documentRef.update("applied", FieldValue.arrayUnion("${movieItem.title}"))
                                        .addOnCompleteListener {
                                            Log.d("ssum", "movies 값 추가 완료")
                                            Toast.makeText(this, "응모가 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show()
                                            finish()
                                        }.addOnFailureListener {
                                            Log.d("ssum", "movies 값 추가 실패")
                                        }
                                }
                            }
                        }
                        if(!searched) {
                            applicant.applied = arrayListOf("${movieItem.title}")
                            MyApplication.db.collection("applicant").document("$name$phone")
                                .set(applicant)
                                .addOnCompleteListener {
                                    Log.d("ssum", "응모자 추가 완료")
                                    Toast.makeText(this, "응모가 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show()
                                    finish()
                                }.addOnFailureListener {
                                    Log.d("ssum", "응모자 추가 실패")
                                }
                        }
                    }.addOnFailureListener {
                        Log.d("ssum", "document 접근 실패")
                    }
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_apply
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        MyApplication.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    //인증 성공
                    Log.d("ssum", "휴대폰 번호 인증 성공")
                    Toast.makeText(this, "휴대폰 인증 성공", Toast.LENGTH_SHORT).show()
                    mode = "verified"
                    updateView()
                } else {
                    //인증 실패
                    Log.d("ssum", "휴대폰 번호 인증 실패")
                    Toast.makeText(this, "휴대폰 인증 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateView() {
        if(mode == "verified") {
            binding.submitBtn.visibility = View.VISIBLE
        }
    }
}