package com.example.cinemaisland.apply

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.example.cinemaisland.BaseActivity
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityApplyBinding

class ApplyActivity : BaseActivity() {
    lateinit var binding: ActivityApplyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplyBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_apply
    }
}