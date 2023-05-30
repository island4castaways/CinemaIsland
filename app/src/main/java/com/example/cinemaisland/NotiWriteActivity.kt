package com.example.cinemaisland

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cinemaisland.databinding.ActivityNotiWriteBinding

class NotiWriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNotiWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}