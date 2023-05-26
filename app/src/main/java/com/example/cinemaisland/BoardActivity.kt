package com.example.cinemaisland

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cinemaisland.databinding.ActivityBoardBinding
import com.google.android.material.tabs.TabLayoutMediator

class BoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = MyPageAdapter(this)
        binding.viewpager.adapter=adapter
        TabLayoutMediator(binding.tabs, binding.viewpager){tab, position ->
            tab.text = "Tab${(position +1)}"
        }.attach()


    }
    class MyPageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        val fragments: List<Fragment>
        init {
            fragments = listOf(NotificationFragment(), OngoingEventFragment(), EndEventFragment())
        }

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

}