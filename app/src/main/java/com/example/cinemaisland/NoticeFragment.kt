package com.example.cinemaisland


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.databinding.FragmentNoticeBinding
import com.example.cinemaisland.notice.NoticeItem
import com.example.cinemaisland.notice.NoticeRecyclerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.ceil


class NoticeFragment : Fragment() {

    private lateinit var nRecyclerView: RecyclerView
    private lateinit var nRecyclerAdapter: NoticeRecyclerAdapter
    private lateinit var noticeItems: ArrayList<NoticeItem>

//    private lateinit var prevButton: Button
//    private lateinit var nextButton: Button
//    private lateinit var pageNumberTextView: TextView
//
//    private var currentPage = 1
//    private val itemsPerPage = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): RelativeLayout {
        // Inflate the layout for this fragment
        val binding = FragmentNoticeBinding.inflate(inflater, container, false)

        binding.writeButton.setOnClickListener {
            val intent = Intent(activity, NotiWriteActivity::class.java)
            startActivity(intent)
        }

        if(MyApplication.managerMode != "on") {
            binding.writeButton.visibility = View.GONE
        }

        var db: FirebaseFirestore = FirebaseFirestore.getInstance()

        // 공지사항 리사이클러뷰
        nRecyclerView = binding.recyclerView

        // 이전 버튼
//        prevButton = binding.btnPrev
//        prevButton.setOnClickListener {
//            if (currentPage > 1) {
//                currentPage--
//                updateNoticeList()
//            }
//        }

        // 다음 버튼
//        nextButton = binding.btnNext
//        nextButton.setOnClickListener {
//            val totalPages = getTotalPages()
//            if (currentPage < totalPages) {
//                currentPage++
//                updateNoticeList()
//            }
//        }

//        // 페이지 번호 텍스트뷰
//        pageNumberTextView = binding.pageNumber

        /* initiate adapter */
        nRecyclerAdapter = NoticeRecyclerAdapter()

        /* initiate recyclerview */
        nRecyclerView.adapter = nRecyclerAdapter
        nRecyclerView.layoutManager = LinearLayoutManager(context)

        /* adapt data */
        noticeItems = ArrayList<NoticeItem>()


//        for (i in 1..30) { // 임의로 30개의 공지사항 아이템을 추가
//            noticeItems.add(NoticeItem("id:${i}", "${i}번째 제목", "날짜", "본문"))
//        }

        db.collection("notice").orderBy("date", Query.Direction.DESCENDING).get()
            .addOnCompleteListener { task ->
                for (document in task.result) {
                    noticeItems.add(document.toObject(NoticeItem::class.java))
                }
                Log.d("ssum", "$noticeItems")
                nRecyclerAdapter.setNoticeList(noticeItems)
            }
        return binding.root
    }

    private fun updateNoticeList() {
//        val start = (currentPage - 1) * itemsPerPage
//        val end = minOf(start + itemsPerPage, noticeItems.size)
//        val sublist = noticeItems.subList(start, end)
//        nRecyclerAdapter.setNoticeList(noticeItems)

//        val totalPages = getTotalPages()
//        pageNumberTextView.text = "페이지 $currentPage / $totalPages"

//        prevButton.isEnabled = currentPage > 1
//        nextButton.isEnabled = currentPage < totalPages
    }

//    private fun getTotalPages(): Int {
//        return ceil(noticeItems.size.toDouble() / itemsPerPage).toInt()
//    }
}

