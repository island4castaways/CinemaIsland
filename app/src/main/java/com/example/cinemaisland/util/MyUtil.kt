package com.example.cinemaisland.util

import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date

fun checkPermission(activity: AppCompatActivity, permissions: Array<String>) {
    val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if(it.all { permission -> permission.value == true }) {
            Toast.makeText(activity, "권한 승인", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "권한 거부", Toast.LENGTH_SHORT).show()
        }
    }

    if(ContextCompat.checkSelfPermission(activity, permissions.get(0)) !== PackageManager.PERMISSION_GRANTED) {
        requestPermissionLauncher.launch(permissions)
    }
}

fun dateTimeToString(date: Date): String {
    val sdf = SimpleDateFormat("yyyyMMdd-HH:mm")
    return sdf.format(date)
}

fun dateToString(date:Date): String{
    val sdf = SimpleDateFormat("yyyyMMdd")
    return sdf.format(date)
}

fun stringToDate(dateString: String): Date {
    val inputFormat = SimpleDateFormat("yyyyMMdd")
    val date = inputFormat.parse(dateString)
    return date
}

fun timeStringToDate(dateString: String): Date {
    val inputFormat = SimpleDateFormat("yyyyMMdd-HH:mm")
    val date = inputFormat.parse(dateString)
    return date
}

fun addNationCode(msg: String): String {
    val firstNumber: String = msg.substring(0, 3)
    var phoneEdit = msg.substring(3)

    when(firstNumber) {
        "010" -> phoneEdit = "+8210$phoneEdit"
    }

    Log.d("ssum", "국가코드로 변경된 번호, $phoneEdit")
    return phoneEdit
}