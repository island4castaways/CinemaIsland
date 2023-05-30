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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class CheckResultActivity : BaseActivity() {
    lateinit var binding: ActivityCheckResultBinding
    val db: FirebaseFirestore = MyApplication.db
    var mode = "default"
    var movies = ArrayList<String>()
    var documentId: String = ""

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
                runBlocking {
                    db.collection("applicant").get()
                        .addOnCompleteListener { task ->
                            val result = task.result
                            for (document in result) {
                                Log.d("ssum", "${document.id}, ${document.data}")
                                if (document.data.getValue("name").toString().equals(name) &&
                                    document.data.getValue("phone").equals(phone) &&
                                    document.data.getValue("email").equals(email)
                                ) {
                                    Log.d("ssum", "Applicant searched")
                                    documentId = document.id.toString()
                                    mode = "searched"

                                    //검색된 응모자 정보에서 당첨된 영화 목록 가져오기
                                    movies = document["movies"] as ArrayList<String>
                                    Log.d("ssum", "won movies : ${movies.toString()}")
                                } else {
                                    Log.d("ssum", "Applicant has no prize")
                                    Toast.makeText(
                                        this@CheckResultActivity,
                                        "입력한 정보로 당첨된 응모 내역이 없습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            makeRecyclerView(movies)
                            updateView()
                        }.addOnFailureListener {
                            Log.d("ssum", "Error getting applicantList, $it")
                        }.await()
                }
            }
        }

        //resetBtn 클릭시
        binding.resetBtn.setOnClickListener {
            Log.d("ssum", "resetBtn Clicked")
            mode = "default"
            updateView()
        }
    }

    private fun updateView() {
        if (mode == "searched") {
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

    private fun makeRecyclerView(moviesList: ArrayList<String>) {
        db.collection("movie").get()
            .addOnCompleteListener { task ->
                for (document in task.result) {
                    for (movie in moviesList) {
                        val itemList = mutableListOf<MovieItem>()
                        if (document.data.getValue("title").equals(movie)) {
                            val item = document.toObject(MovieItem::class.java)
                            item.id = document.id
                            itemList.add(item)
                        }
                        binding.resultRecyclerView.layoutManager = LinearLayoutManager(this)
                        binding.resultRecyclerView.adapter = ResultRecyclerAdapter(this, itemList)
                    }
                }
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