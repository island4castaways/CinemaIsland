package com.example.cinemaisland.movie

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.databinding.ActivityMovieBinding
import com.example.cinemaisland.model.MovieItem
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MovieActivity : AppCompatActivity() {

    private lateinit var mrecyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    lateinit var binding: ActivityMovieBinding
    private lateinit var imageViewPoster: ImageView
    private lateinit var storageRef: StorageReference
    private val imageList = mutableListOf<MovieItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageViewPoster = binding.imageViewPoster
        // 스토리지 객체 만들고 참조
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child("movie")

        movieAdapter = MovieAdapter(imageList)

        // 이미지 폴더의 모든 파일을 가져오기
        imageRef.listAll()
            .addOnSuccessListener { listResult ->
                for (item in listResult.items) {
                    item.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
//                        val movieItem = MovieItem(imageUrl, "Movie Title")
//                        imageList.add(movieItem)
                        // RecyclerView에 데이터 갱신
                        movieAdapter.updateData(imageList)
                    }.addOnFailureListener { exception ->
                        Log.w("ssum", "오류: $exception")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ssum", "오류: $exception")
            }

        mrecyclerView = binding.recyclerViewMovie

        // RecyclerView를 Grid 형태로 표시하기 위해 GridLayoutManager를 사용합니다.
        val layoutManager = GridLayoutManager(this, 2)
        mrecyclerView.layoutManager = layoutManager
        mrecyclerView.adapter = movieAdapter
    }
}
