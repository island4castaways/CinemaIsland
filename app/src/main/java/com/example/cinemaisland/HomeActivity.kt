package com.example.cinemaisland

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<<<< Temporary merge branch 1
import android.widget.FrameLayout
import com.example.cinemaisland.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_home
=========
import android.widget.TextView
import com.example.cinemaisland.databinding.ActivityHomeBinding
import android.telephony.PhoneNumberFormattingTextWatcher


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
>>>>>>>>> Temporary merge branch 2
    }
}