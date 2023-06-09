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
import com.example.cinemaisland.util.addNationCode
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

            phone = binding.inputPhone.text.toString()

            val callbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Toast.makeText(this@ApplyActivity, "인증 코드가 전송되었습니다. 90초 이내에 입력해주세요.", Toast.LENGTH_SHORT).show()
                    Log.d("ssum", "Verification completed")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.e("ssum", "$p0")
                    Log.d("ssum", "Verification failed")
                    Toast.makeText(this@ApplyActivity, "인증 실패", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    MyApplication.verificationId = p0
                    MyApplication.resendToken = p1

                    binding.authView.visibility = View.VISIBLE

                    //authBtn 클릭시
                    binding.authBtn.setOnClickListener {
                        val inputAuth = binding.inputAuth.text.toString()
                        val credential = PhoneAuthProvider.getCredential(MyApplication.verificationId, inputAuth)
                        signInWithPhoneAuthCredential(credential)
                    }
                }
            }

            auth.setLanguageCode("kr")
            val optionsCompat = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(addNationCode(phone))
                .setTimeout(90L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(optionsCompat)
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

                //applicant에 응모자 정보 저장
                db.collection("applicant").get()
                    .addOnCompleteListener { task ->
                        for(document in task.result) {
                            //저장된 응모자 정보가 있는지 확인
                            if(document.id == "$name$phone") {
                                Log.d("ssum", "응모자 검색 결과, $name$phone")
                                searched = true

                                //검색된 응모자의 movies 필드에 현재 영화 정보가 존재하는지 확인
                                val array = document["applied"] as ArrayList<String>
                                if(array.contains("${movieItem.title}@${movieItem.director}")) {
                                    //이미 응모된 영화일 경우
                                    Toast.makeText(this, "이미 응모된 시사회입니다.", Toast.LENGTH_SHORT).show()
                                } else {
                                    //movies 필드에 영화 정보 없는 경우 추가
                                    documentRef.update("applied", FieldValue.arrayUnion("${movieItem.title}@${movieItem.director}"))
                                        .addOnCompleteListener {
                                            Log.d("ssum", "movies 값 추가 완료")
                                            Toast.makeText(this, "시사회 응모가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                            finish()    //Activity 종료
                                        }.addOnFailureListener {
                                            Log.d("ssum", "movies 값 추가 실패")
                                        }

                                    //movie의 applicants 필드에 응모자 추가
                                    db.collection("movie").document("${movieItem.title}@${movieItem.director}")
                                        .update("applicants", FieldValue.arrayUnion("$name$phone"))
                                        .addOnSuccessListener {
                                            Log.d(
                                                "ssum",
                                                "movie ${movieItem.title}@${movieItem.director}, " +
                                                        "applicants field에 $name$phone 추가 완료"
                                            )
                                        }.addOnFailureListener { 
                                            Log.d("ssum", "movie, applicants field 추가 실패")
                                        }
                                }
                            }
                        }
                        //저장된 응모자 정보가 없을 경우 추가
                        if(!searched) {
                            applicant.applied = arrayListOf("${movieItem.title}@${movieItem.director}")
                            //applicant에 응모자 document 추가
                            MyApplication.db.collection("applicant").document("$name$phone")
                                .set(applicant)
                                .addOnCompleteListener {
                                    Log.d("ssum", "응모자 추가 완료")
                                    Toast.makeText(this, "시사회 응모가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                    finish()
                                }.addOnFailureListener {
                                    Log.d("ssum", "응모자 추가 실패")
                                }

                            //movie의 applicants 필드에 응모자 추가
                            db.collection("movie").document("${movieItem.title}@${movieItem.director}")
                                .update("applicants", FieldValue.arrayUnion("$name$phone"))
                                .addOnSuccessListener {
                                    Log.d(
                                        "ssum",
                                        "movie ${movieItem.title}@${movieItem.director}, " +
                                                "applicants field에 $name$phone 추가 완료"
                                    )
                                }.addOnFailureListener { 
                                    Log.d("ssum", "movie, applicants field에 추가 실패")
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