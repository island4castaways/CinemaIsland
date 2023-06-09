package com.example.cinemaisland.notice

import com.google.firebase.Timestamp

//class NoticeItem {
//    val id: String? = null
//    val title: String? = null
//    val date: String? = null
//    val content: String? = null
//}

data class NoticeItem(
    var id: String? = null,
    var title: String? = null,
    var date: Timestamp? = null,
    var content: String? = null
)
