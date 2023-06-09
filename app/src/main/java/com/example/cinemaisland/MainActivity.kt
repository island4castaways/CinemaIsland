package com.example.cinemaisland

import android.os.Bundle
import android.widget.FrameLayout
import com.example.cinemaisland.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }
}
