package com.example.cinemaisland.check

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.cinemaisland.databinding.ActivityCheckResultBinding

class CheckResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //submitBtn 클릭시
        binding.submitBtn.setOnClickListener {
            Log.d("ssum", "submitBtn Clicked")
            //입력 내용 제출, DB에서 검색


            //inputView 보이지 않게 설정
            binding.inputView.visibility = View.GONE

            //resultView에 검색 정보 출력


            //resultView 보이게 설정
            binding.resultView.visibility = View.VISIBLE
        }

        //resetBtn 클릭시
        binding.resetBtn.setOnClickListener {
            Log.d("ssum", "resetBtn Clicked")
            //resultView 보이지 않게 설정
            binding.resultView.visibility = View.GONE

            //inputView 보이게 설정
            binding.inputView.visibility = View.VISIBLE
        }
    }
}