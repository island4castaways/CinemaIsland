package com.example.cinemaisland.check

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemaisland.BaseActivity
import com.example.cinemaisland.MyApplication
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityCheckResultBinding
import com.example.cinemaisland.model.MovieItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CheckResultActivity : BaseActivity() {
    lateinit var binding: ActivityCheckResultBinding
    val db: FirebaseFirestore = MyApplication.db
    var mode = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckResultBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

        //submitBtn 클릭시
        binding.submitBtn.setOnClickListener {
            Log.d("ssum", "submitBtn Clicked")
            //입력 정보 가져오기
            val name = binding.inputName.text.toString()
            val phone = binding.inputPhone.text.toString()
            val email = binding.inputEmail.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                //DB에서 확인
                var movies = searchApplicant(name, phone, email)
                makeRecyclerView(movies)
            }
            updateView()
        }

        //resetBtn 클릭시
        binding.resetBtn.setOnClickListener {
            Log.d("ssum", "resetBtn Clicked")
            mode = "default"
            updateView()
        }
    }

    private fun updateView() {
        if (mode == "listed") {
            //inputView 보이지 않게 설정
            binding.inputView.visibility = View.GONE
            //resultView 보이게 설정
            binding.resultView.visibility = View.VISIBLE
        } else if (mode == "default") {
            //resultView 보이지 않게 설정
            binding.resultView.visibility = View.GONE
            //inputView 보이게 설정
            binding.inputView.visibility = View.VISIBLE
        }
    }

    private suspend fun searchApplicant(name: String, phone: String, email: String): ArrayList<String> {
        val movies = ArrayList<String>()
        val querySnapshot = db.collection("applicant")
            .whereEqualTo("name", name)
            .whereEqualTo("phone", phone)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                mode = "searched"
                Log.d("ssum", "mode, $mode")
            }
            .addOnFailureListener {
                Log.d("ssum", "Error getting documents")
            }
            .await()

        if(!querySnapshot.isEmpty) {
            val document = querySnapshot.documents[0]

            val wonList = document["won"] as? ArrayList<String>
            if(wonList != null) {
                Log.d("ssum", "won movies : $wonList")
                movies.addAll(wonList)
            } else {
                Log.d("ssum", "Applicants has no movie on won list")
                Toast.makeText(
                    this@CheckResultActivity,
                    "입력한 정보로 당첨된 응모 내역이 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Log.d("ssum", "No applicant has searched")
            Toast.makeText(
                this@CheckResultActivity,
                "입력한 정보로 존재하는 응모 내역이 없습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        return movies
    }

    private fun makeRecyclerView(moviesList: ArrayList<String>) {
        //응모자의 movies list에 있는 영화를 movie collection에서 가져오기
        db.collection("movie").get()
            .addOnCompleteListener { task ->
                val itemList = mutableListOf<MovieItem>()
                for (document in task.result) {
                    for (movie in moviesList) {
                        if (document.data.getValue("title") == "$movie") {
                            val item = document.toObject(MovieItem::class.java)
                            itemList.add(item)
                            Log.d("ssum", "${itemList}")
                        }
                    }
                }
                binding.resultRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.resultRecyclerView.adapter = ResultRecyclerAdapter(this, itemList)
                mode = "listed"
                Log.d("ssum", "mode, $mode")
            }.addOnFailureListener {
                Log.d("ssum", "Error getting moviesList, $it")
                Toast.makeText(
                    this,
                    "서버로부터 데이터를 가져오는데 실패했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_check_result
    }
}