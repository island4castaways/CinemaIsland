package com.example.cinemaisland.raffle

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.example.cinemaisland.BaseActivity
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityRaffleBinding

class raffleActivity : BaseActivity() {
    lateinit var binding: ActivityRaffleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaffleBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

        binding.raffleBtn.setOnClickListener {
            Log.d("ssum", "raffleBtn clicked")
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_raffle
    }
}