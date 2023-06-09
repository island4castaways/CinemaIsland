package com.example.cinemaisland.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import com.example.cinemaisland.BaseActivity
import com.example.cinemaisland.HomeActivity
import com.example.cinemaisland.MyApplication
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    /* DB에 저장된 관리자 정보
    managerId : admin
    managerPw : admin */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        //툴바 적용
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

        updateView()

        //로그인 버튼 클릭시
        binding.loginBtn.setOnClickListener {
            Log.d("ssum", "loginBtn Clicked")
            //입력 정보 가져오기
            val adminId = binding.inputId.text.toString()
            val adminPw = binding.inputPw.text.toString()
            //id, pw DB에서 확인
            db.collection("admin_account").get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        Log.d("ssum", "${document.id}, ${document.data}")
                        if(document.data.getValue("id").toString().equals(adminId) &&
                                document.data.getValue("password").toString().equals(adminPw)) {
                            Log.d("ssum", "Manager Login successed")
                            MyApplication.managerMode = "on"
                            updateView()
                        } else {
                            //로그인 실패시
                            Log.d("ssum", "Manager Login Failed")
                            Toast.makeText(this, "로그인 정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener {
                    Log.d("ssum", "Error getting documents, $it")
                }
        }

        //로그아웃 버튼 클릭시
        binding.logoutBtn.setOnClickListener {
            Log.d("ssum", "logoutBtn Clicked")
            MyApplication.managerMode = "off"
            //EditTextView 비우기
            binding.inputId.text.clear()
            binding.inputPw.text.clear()
            updateView()

            //로그아웃 버튼 클릭시 홈으로 이동
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun updateView() {
        if(MyApplication.managerMode == "on") {   //로그인 상태
            //loginView 보이지 않게 설정
            binding.loginView.visibility = View.GONE
            //title text 변경
            binding.title.text = "관리자 모드"
            //logoutBtn 보이게 설정
            binding.logoutBtn.visibility = View.VISIBLE
        } else if(MyApplication.managerMode == "off") {   //로그아웃 상태
            //loginView 보이게 설정
            binding.loginView.visibility = View.VISIBLE
            //title text 변경
            binding.title.text = "관리자 모드 로그인"
            //logoutBtn 보이지 않게 설정
            binding.logoutBtn.visibility = View.GONE
        }

    }
    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }
}