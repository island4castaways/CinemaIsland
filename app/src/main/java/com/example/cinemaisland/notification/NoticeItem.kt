package com.example.cinemaisland.notification

data class NoticeItem(
    @field:JvmField
    val resourceId: String,
    val notice_title: String,
    val notice_date: String,
    val notice_content:String
) {
    // 기본 getter, setter가 자동으로 생성됩니다.
}