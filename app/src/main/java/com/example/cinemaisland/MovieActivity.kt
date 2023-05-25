package com.example.cinemaisland

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cinemaisland.databinding.ActivityMovieBinding

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}