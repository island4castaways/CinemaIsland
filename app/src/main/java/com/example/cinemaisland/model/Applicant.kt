package com.example.cinemaisland.model

import java.util.Date

class Applicant {
    var id: String? = null
    var name: String? = null
    var phone: String? = null
    var email: String? = null
    var birthDate: Date? = null
    var applied: ArrayList<String>? = null
    var won: ArrayList<String>? = null
}