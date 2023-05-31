package com.example.cinemaisland.apply

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.example.cinemaisland.BaseActivity
import com.example.cinemaisland.MyApplication
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityApplyBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ApplyActivity : BaseActivity() {
    lateinit var binding: ActivityApplyBinding
    lateinit var name: String
    lateinit var phone: String
    lateinit var email: String
    lateinit var birthDate: String
    var mode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplyBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

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

            val optionsCompat = PhoneAuthOptions.newBuilder(MyApplication.auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(optionsCompat)
            MyApplication.auth.setLanguageCode("kr")
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
                //DB에 저장

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