package com.example.cinemaisland.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityRaffleBinding

class RaffleActivity : AppCompatActivity() {
    lateinit var binding: ActivityRaffleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaffleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}