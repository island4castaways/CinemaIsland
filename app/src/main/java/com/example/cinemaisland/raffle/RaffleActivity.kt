package com.example.cinemaisland.raffle

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.example.cinemaisland.BaseActivity
import com.example.cinemaisland.MyApplication
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityRaffleBinding
import com.example.cinemaisland.model.MovieItem
import com.example.cinemaisland.util.dateTimeToString
import com.example.cinemaisland.util.dateToString
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class RaffleActivity : BaseActivity() {
    lateinit var binding: ActivityRaffleBinding
    val db: FirebaseFirestore = MyApplication.db
    var movieTitle = ""
    var applicantText = ""
    var applicantList: ArrayList<String>? = null
    var winnerText = ""
    var winnerList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaffleBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

        val movieItem = intent.getSerializableExtra("movieItem") as? MovieItem
        movieTitle = movieItem!!.title!!
        binding.movieTitle.text = movieTitle
        binding.movieSchedule.text = dateToString(movieItem.schedule!!)
        binding.movieRaffleDate.text = dateTimeToString(movieItem.raffleDate!!)

        val querySnapshot = db.collection("movie")
            .whereEqualTo("title", "$movieTitle").get()
            .addOnSuccessListener { querySnapshot ->
                Log.d("ssum", "movie $movieTitle document accessed")
                for (document in querySnapshot) {
                    //winners 배열 값 존재 확인 -> 추첨 여부 확인
                    winnerList = document["winners"] as? ArrayList<String>
                    if (winnerList != null) {
                        //winner 값이 있을 경우 -> 이미 추첨 완료
                        binding.raffleBtn.visibility = View.GONE
                        for (winner in winnerList!!) {
                            Log.d("ssum", "$winner")
                            winnerText += "$winner\n"
                        }
                    } else {
                        binding.resultView.visibility = View.GONE
                    }

                    //applicants 배열 값 가져오기
                    applicantList = document["applicants"] as? ArrayList<String>
                    if (applicantList != null) {
                        for (applicant in applicantList!!) {
                            Log.d("ssum", "$applicant")
                            applicantText += "$applicant\n"
                        }
                    } else {
                        Log.d("ssum", "응모자 없음")
                        applicantText = "응모자 없음"
                    }
                }
                binding.winnerList.text = winnerText
                binding.applicantList.text = applicantText
            }.addOnFailureListener {
                Log.d("ssum", "Error getting documents, $it")
            }

        binding.raffleBtn.setOnClickListener {
            Log.d("ssum", "raffleBtn clicked")
            binding.raffleBtn.visibility = View.GONE
            winnerList = runRaffle()
            updateWinnerList("$movieTitle@${movieItem.director}", winnerList!!)
            updateWonList(winnerList!!, movieItem)
            for(winner in winnerList!!) {
                winnerText += "$winner\n"
            }
            binding.winnerList.text = winnerText
            binding.resultView.visibility = View.VISIBLE
        }
    }

    private fun runRaffle(): ArrayList<String> {
        val winnerList = ArrayList<String>()
        //applicantList의 index로 사용할 randomNumber 20개 생성
        val random = java.util.Random()
        val numbers = mutableListOf<Int>()
        val range = 1..(applicantList?.size ?: 0)
        if ((applicantList?.size ?: 0) >= 20) {
            while (numbers.size < 20) {
                val randomNumber = random.nextInt(range.last - range.first + 1) + range.first
                if (!numbers.contains(randomNumber)) {
                    numbers.add(randomNumber)
                }
            }
            Log.d("ssum", "$numbers")
            //생성된 randomNumber의 index에 해당하는 applicant를 winnerList에 추가
            for (number in numbers) {
                winnerList.add(applicantList!![number - 1])
                Log.d("ssum", "${applicantList!![number - 1]}")
            }
        } else {
            winnerList.addAll(applicantList!!)
            Log.d("ssum", "응모자 수 부족, 전원 당첨")
        }
        return winnerList
    }

    private fun updateWinnerList(documentId: String, winnerList: ArrayList<String>) {
        //DB에 winnerList 저장
        val documentRef = db.collection("movie").document(documentId)
        documentRef.update("winners", winnerList)
            .addOnCompleteListener {
                Log.d("ssum", "winners 추가 완료")
                Toast.makeText(this, "추첨 결과 저장 완료", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Log.d("ssum", "winners 추가 실패")
            }
    }
    
    private fun updateWonList(winnerList: ArrayList<String>, movieItem: MovieItem) {
        //applicant의 won 필드에 영화 정보 추가
        for(winner in winnerList) {
            val documentId = "$winner"
            db.collection("applicant").document(documentId)
                .update("won", FieldValue.arrayUnion("${movieItem.title}@${movieItem.director}"))
            Log.d("ssum", "applicant $documentId, won에 ${movieItem.title}@${movieItem.director} 추가 완료")
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_raffle
    }
}