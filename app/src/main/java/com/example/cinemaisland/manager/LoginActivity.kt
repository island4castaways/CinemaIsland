package com.example.cinemaisland.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 버튼 클릭시
        binding.loginBtn.setOnClickListener {
            Log.d("ssum", "loginBtn Clicked")
            //입력 내용 제출, DB에서 정보 확인


            //로그인 완료시
            //loginView 보이지 않게 설정
            binding.loginView.visibility = View.GONE
            //title text 변경
            binding.title.text = "관리자 모드"
            //logoutBtn 보이게 설정
            binding.logoutBtn.visibility = View.VISIBLE

            //로그인 실패시, 조건문 추가
            Toast.makeText(this, "로그인 정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
        }

        //로그아웃 버튼 클릭시
        binding.logoutBtn.setOnClickListener {
            Log.d("ssum", "logoutBtn Clicked")

            //loginView 보이게 설정
            binding.loginView.visibility = View.VISIBLE
            //title text 변경
            binding.title.text = "관리자 모드 로그인"
            //logoutBtn 보이지 않게 설정
            binding.logoutBtn.visibility = View.GONE
        }
    }
}