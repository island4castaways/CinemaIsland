package com.example.cinemaisland

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemaisland.MyApplication.Companion.db
import com.example.cinemaisland.databinding.EventRecyclerviewBinding
import com.example.cinemaisland.databinding.FragmentOngoingEventBinding

import com.example.cinemaisland.model.MovieItem
import com.example.cinemaisland.util.timeStringToDate
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

var db: FirebaseFirestore = FirebaseFirestore.getInstance()

class OngoingEventFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOngoingEventBinding.inflate(inflater, container, false)
        val movieList: MutableList<MovieItem> = mutableListOf()

        db.collection("movie")
            .orderBy("raffleDate", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task->
                for(document in task.result){
                    if(document.toObject(MovieItem::class.java).raffleDate!! > Date(System.currentTimeMillis())){
                        movieList.add(document.toObject(MovieItem::class.java))
                    }

                }
                Log.d("ssum", "${movieList.toString()}")
                binding.eventRecyclerView.adapter!!.notifyDataSetChanged()

        }

        binding.eventRecyclerView.layoutManager = GridLayoutManager(context,2)
        binding.eventRecyclerView.adapter = MyAdapter(movieList)

        binding.movieWriteButton.setOnClickListener {
            val intent = Intent(activity, MovieWriteActivity::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return binding.root



    }


}

class EventViewHolder(val binding: EventRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas:MutableList<MovieItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 넘어온 데이터(항목 구성 데이터)에 대한 개수 판단을 위한 함수(자동 호출)

    override fun getItemCount(): Int {
        return datas.size
    }

    // 항목 뷰를 가지고 뷰 홀더를 준비하기 위한 함수(자동 호출)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder = EventViewHolder(EventRecyclerviewBinding.inflate(
        LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as EventViewHolder).binding
        val movieItem = datas[position]
        //뷰에 데이터를 출력
        binding.genreTextView.text = "장르: ${movieItem.genre}"
        Glide.with(binding.moviePosterImageView.context).load(movieItem.imageUrl).into(binding.moviePosterImageView)
        binding.scheduleTextView.text = "시사회 날짜: ${movieItem.schedule.toString()}"
        binding.raffleDateTextView.text = "추첨 날짜 : ${movieItem.raffleDate.toString()}"

        binding.moviePosterImageView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MovieDetailActivity::class.java )
            intent.putExtra("movieItem",movieItem)
            holder.itemView.context.startActivity(intent)
        }
    }
}