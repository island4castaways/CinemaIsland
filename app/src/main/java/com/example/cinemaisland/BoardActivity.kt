package com.example.cinemaisland

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cinemaisland.databinding.ActivityBoardBinding
import com.google.android.material.tabs.TabLayoutMediator

lateinit var toggle: ActionBarDrawerToggle

class BoardActivity : BaseActivity() {
    lateinit var binding: ActivityBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        //툴바 적용
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)


//        setSupportActionBar(binding.toolbar)
//        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toggle.syncState()

        val adapter = MyPageAdapter(this)
        binding.viewpager.adapter=adapter
        TabLayoutMediator(binding.tabs, binding.viewpager){tab, position ->
            when(position){
                0-> tab.text="공지사항"
                1-> tab.text="진행중인 응모"
                2-> tab.text="완료된 응모"
                else-> true
            }
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

    override fun getLayoutResId(): Int {
        return R.layout.activity_board
    }


}