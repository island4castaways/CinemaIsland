package com.example.cinemaisland

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.databinding.ActivityMainBinding
import com.example.cinemaisland.notification.NoticeRecyclerAdapter
import com.example.cinemaisland.notification.NoticeItem
import kotlin.math.ceil

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
