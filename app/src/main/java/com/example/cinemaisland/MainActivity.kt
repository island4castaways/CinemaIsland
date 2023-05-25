package com.example.cinemaisland

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.notification.NoticeRecyclerAdapter
import com.example.cinemaisland.notification.NoticeItem
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var nRecyclerView: RecyclerView
    private lateinit var nRecyclerAdapter: NoticeRecyclerAdapter
    private lateinit var noticeItems: ArrayList<NoticeItem>

    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var pageNumberTextView: TextView

    private var currentPage = 1
    private val itemsPerPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 공지사항 리사이클러뷰
        nRecyclerView = findViewById(R.id.recyclerView)

        // 이전 버튼
        prevButton = findViewById(R.id.btn_prev)
        prevButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                updateNoticeList()
            }
        }

        // 다음 버튼
        nextButton = findViewById(R.id.btn_next)
        nextButton.setOnClickListener {
            val totalPages = getTotalPages()
            if (currentPage < totalPages) {
                currentPage++
                updateNoticeList()
            }
        }

        // 페이지 번호 텍스트뷰
        pageNumberTextView = findViewById(R.id.page_number)

        /* initiate adapter */
        nRecyclerAdapter = NoticeRecyclerAdapter()

        /* initiate recyclerview */
        nRecyclerView.adapter = nRecyclerAdapter
        nRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        /* adapt data */
        noticeItems = ArrayList()
        for (i in 1..30) { // 임의로 30개의 공지사항 아이템을 추가
            noticeItems.add(NoticeItem("id:${i}", "${i}번째 제목", "날짜", "본문"))
        }
        updateNoticeList()
    }

    private fun updateNoticeList() {
        val start = (currentPage - 1) * itemsPerPage
        val end = minOf(start + itemsPerPage, noticeItems.size)
        val sublist = noticeItems.subList(start, end)
        nRecyclerAdapter.setNoticeList(sublist)

        val totalPages = getTotalPages()
        pageNumberTextView.text = "페이지 $currentPage / $totalPages"

        prevButton.isEnabled = currentPage > 1
        nextButton.isEnabled = currentPage < totalPages
    }

    private fun getTotalPages(): Int {
        return ceil(noticeItems.size.toDouble() / itemsPerPage).toInt()
    }
}
