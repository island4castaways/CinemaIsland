package com.example.cinemaisland.model

import java.util.Date

class MovieItem {
    var id: String? = null
    var title: String? = null
    var raffleDate: Date? = null // 추첨일
    var genre: String? = null
    var director: String? = null
    var rating: Int = 0 //관람등급
    var schedule: Date? = null //상영 시간
    var details: String? = null
    var imageUrl: String? = null
    var applicants: ArrayList<String>? = null
    var winners: ArrayList<String>? = null
}