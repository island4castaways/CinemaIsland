package com.example.cinemaisland

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.cinemaisland.databinding.ActivityMovieDetailBinding
import com.example.cinemaisland.model.MovieItem
import java.util.Date

class MovieDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

        val movieItem = intent.getSerializableExtra("movieItem") as? MovieItem

        if (movieItem != null) {
            binding.titleText.text = "영화제목: ${movieItem.title}"
            binding.directorText.text = "감독: ${movieItem.director}"
            binding.genreText.text = "장르: ${movieItem.genre}"
            binding.scheduleText.text = "시사회 날짜: ${movieItem.schedule.toString()}"
            binding.raffleText.text = "추첨 날짜: ${movieItem.raffleDate.toString()}"
            binding.detailText.text = "줄거리: ${movieItem.details}"
            Glide.with(binding.moviePosterImageView.context).load(movieItem.imageUrl)
                .into(binding.moviePosterImageView)

            if (movieItem.raffleDate!! <= Date(System.currentTimeMillis())) {
                binding.applyBtn.visibility = View.GONE
                binding.movieDeleteBtn.setOnClickListener {
                    MyApplication.db.collection("movie")
                        .document("${movieItem!!.title}@${movieItem!!.director}")
                        .delete()

                    val endEventFragment: EndEventFragment = EndEventFragment()
                    val manager: FragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = manager.beginTransaction()
                    transaction.replace(R.id.viewpager, endEventFragment).commit()
                }
            } else {
                binding.movieDeleteBtn.setOnClickListener {
                    MyApplication.db.collection("movie")
                        .document("${movieItem!!.title}@${movieItem!!.director}")
                        .delete()

                    val ongoingEventFragment: OngoingEventFragment = OngoingEventFragment()
                    val manager: FragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = manager.beginTransaction()
                    transaction.replace(R.id.viewpager, ongoingEventFragment).commit()
                }
            }
        }
    }
    override fun getLayoutResId(): Int {
        return R.layout.activity_movie_detail
    }
}