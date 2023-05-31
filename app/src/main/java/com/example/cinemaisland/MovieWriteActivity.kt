package com.example.cinemaisland

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ToggleButton
import com.example.cinemaisland.databinding.ActivityMovieWriteBinding

class MovieWriteActivity : AppCompatActivity() {

    lateinit var genreToggleButton: ToggleButton
    lateinit var genreMenuLayout: LinearLayout
    lateinit var binding: ActivityMovieWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        genreToggleButton = findViewById(R.id.genreToggleButton)
//        genreMenuLayout = findViewById(R.id.genreMenuLayout)
//
//        //토글 버튼 클릭시 아래 메뉴
//        genreToggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                genreMenuLayout.visibility = View.VISIBLE
//            } else {
//                genreMenuLayout.visibility = View.GONE
//            }
//        }
    }
}
