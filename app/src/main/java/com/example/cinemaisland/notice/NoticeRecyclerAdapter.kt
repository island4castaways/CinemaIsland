package com.example.cinemaisland.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.R

class NoticeRecyclerAdapter : RecyclerView.Adapter<NoticeRecyclerAdapter.ViewHolder>() {

    private var noticeList: ArrayList<NoticeItem> = ArrayList()
//    private var currentPage: Int = 1
//    private val itemsPerPage: Int = 10
    private var expandedItemPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notice_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(noticeList[position])
    }

    fun setNoticeList(list: List<NoticeItem>) {
        noticeList.clear()
        noticeList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noticeTitle: TextView = itemView.findViewById(R.id.notice_title)
        private val noticeDate: TextView = itemView.findViewById(R.id.notice_date)
        private val noticeContent: TextView = itemView.findViewById(R.id.notice_content)

        init {
            noticeTitle.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (expandedItemPosition == position) {
                        // 현재 클릭된 아이템이 이미 펼쳐진 상태라면 숨김 처리
                        expandedItemPosition = -1
                    } else {
                        // 클릭된 아이템의 위치를 저장하여 펼침 처리
                        expandedItemPosition = position
                    }
                    notifyDataSetChanged()
                }
            }
        }

        fun onBind(item: NoticeItem) {
            noticeTitle.text = item.title
            noticeDate.text = item.date
            noticeContent.text = item.content

            // 펼쳐진 아이템인 경우 본문을 보여주고, 그렇지 않은 경우 숨김 처리
            if (expandedItemPosition == adapterPosition) {
                noticeContent.visibility = View.VISIBLE
            } else {
                noticeContent.visibility = View.GONE
            }
        }
    }

    private fun updateNoticeList() {
//        val start = (currentPage - 1) * itemsPerPage
//        val end = minOf(start + itemsPerPage, noticeList.size)
//        val sublist = noticeList.subList(start, end)
//        notifyDataSetChanged()
    }

//    fun goToPreviousPage() {
//        if (currentPage > 1) {
//            currentPage--
//            updateNoticeList()
//        }
//    }
//
//    fun goToNextPage() {
//        val totalPages = getTotalPages()
//        if (currentPage < totalPages) {
//            currentPage++
//            updateNoticeList()
//        }
//    }
//
//    private fun getTotalPages(): Int {
//        return (noticeList.size + itemsPerPage - 1) / itemsPerPage
//    }
}
