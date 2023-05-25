package com.example.cinemaisland.check

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cinemaisland.databinding.ActivityCheckResultBinding
import com.google.firebase.firestore.FirebaseFirestore

class CheckResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckResultBinding
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mode = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //submitBtn 클릭시
        binding.submitBtn.setOnClickListener {
            Log.d("ssum", "submitBtn Clicked")
            //입력 정보 가져오기
            val name = binding.inputName.text.toString()
            val phone = binding.inputPhone.text.toString()
            val email = binding.inputEmail.text.toString()
            //DB에서 확인
            db.collection("winner").get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        Log.d("ssum", "${document.id}, ${document.data}")
                        if(document.data.getValue("name").toString().equals(name) &&
                                document.data.getValue("phone").equals(phone) &&
                                document.data.getValue("email").equals(email)) {
                            Log.d("ssum", "Winner searched")
                            mode = "searched"
                            updateView()
                        } else {
                            Log.d("ssum", "Applicant has no prize")
                            Toast.makeText(this, "입력한 정보로 당첨된 응모 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener {
                    Log.d("ssum", "Error getting documents, $it")
                }

            //resultView에 검색 정보 출력

        }

        //resetBtn 클릭시
        binding.resetBtn.setOnClickListener {
            Log.d("ssum", "resetBtn Clicked")
            mode = "default"
            updateView()
        }
    }

    fun updateView() {
        if(mode == "searched") {
            //inputView 보이지 않게 설정
            binding.inputView.visibility = View.GONE
            //resultView 보이게 설정
            binding.resultView.visibility = View.VISIBLE
        } else if(mode == "default") {
            //resultView 보이지 않게 설정
            binding.resultView.visibility = View.GONE
            //inputView 보이게 설정
            binding.inputView.visibility = View.VISIBLE
        }
    }
}