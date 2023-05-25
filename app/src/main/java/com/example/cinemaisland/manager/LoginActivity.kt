package com.example.cinemaisland.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityLoginBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mode = "logout"

    /* DB에 저장된 관리자 정보
    managerId : admin
    managerPw : admin */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 버튼 클릭시
        binding.loginBtn.setOnClickListener {
            Log.d("ssum", "loginBtn Clicked")
            //입력 정보 가져오기
            val managerId = binding.idInput.text.toString()
            val managerPw = binding.passwordInput.text.toString()
            //id, pw DB에서 확인
            db.collection("admin_account").get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        Log.d("ssum", "${document.id}, ${document.data}")
                        if(document.data.getValue("id").toString().equals(managerId) &&
                                document.data.getValue("password").toString().equals(managerPw)) {
                            Log.d("ssum", "Manager Login successed")
                            mode = "login"
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
            mode = "logout"
            //EditTextView 비우기
            binding.idInput.text.clear()
            binding.passwordInput.text.clear()
            updateView()
        }
    }

    fun updateView() {
        if(mode === "login") {   //로그인 상태
            //loginView 보이지 않게 설정
            binding.loginView.visibility = View.GONE
            //title text 변경
            binding.title.text = "관리자 모드"
            //logoutBtn 보이게 설정
            binding.logoutBtn.visibility = View.VISIBLE
        } else if(mode === "logout") {   //로그아웃 상태
            //loginView 보이게 설정
            binding.loginView.visibility = View.VISIBLE
            //title text 변경
            binding.title.text = "관리자 모드 로그인"
            //logoutBtn 보이지 않게 설정
            binding.logoutBtn.visibility = View.GONE
        }
    }
}