package com.example.cinemaisland.notice

data class NoticeItem(
    @field:JvmField
    val resourceId: String,
    val notice_title: String,
    val notice_date: String,
    val notice_content: String
) {
    var isExpanded: Boolean = false
}
