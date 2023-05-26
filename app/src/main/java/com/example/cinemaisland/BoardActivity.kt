package com.example.cinemaisland

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cinemaisland.databinding.ActivityBoardBinding
import com.google.android.material.tabs.TabLayoutMediator

lateinit var toggle: ActionBarDrawerToggle

class BoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        val adapter = MyPageAdapter(this)
        binding.viewpager.adapter=adapter
        TabLayoutMediator(binding.tabs, binding.viewpager){tab, position ->
            tab.text = "Tab${(position +1)}"
        }.attach()



        binding.navigation.setNavigationItemSelectedListener{menuItem->
            when(menuItem.itemId){
                R.id.home->{
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.board ->{
                    val intent = Intent(this, BoardActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.winner ->{
                    val intent = Intent(this, MovieActivity::class.java)    //아직 없어서 임의 지정
                    startActivity(intent)
                    true
                }
                else ->false
            }
        }


    }
    class MyPageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        val fragments: List<Fragment>
        init {
            fragments = listOf(NotificationFragment(), OngoingEventFragment(), EndEventFragment())
        }

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}