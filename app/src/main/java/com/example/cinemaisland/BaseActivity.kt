package com.example.cinemaisland

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemaisland.check.CheckResultActivity
import com.example.cinemaisland.databinding.ActivityBaseBinding
import com.example.cinemaisland.databinding.ActivityBoardBinding
import com.example.cinemaisland.movie.MovieActivity


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBaseBinding.inflate(layoutInflater)

        super.setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(binding.toolbar);

        if(useToolbar()){
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.baseLayout, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

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
                    val intent = Intent(this, CheckResultActivity::class.java)
                    startActivity(intent)
                    true
                }
                else ->false
            }
        }

        setContentView(binding.root)
    }

    //툴바를 사용할지 말지 정함
    protected fun useToolbar(): Boolean {
        return true
    }

    abstract fun getLayoutResId(): Int

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}