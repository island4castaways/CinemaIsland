package com.example.cinemaisland.check

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.databinding.ItemResultBinding
import com.example.cinemaisland.model.MovieItem
import com.example.cinemaisland.util.dateToString

class ResultViewHolder(val binding: ItemResultBinding): RecyclerView.ViewHolder(binding.root)

class ResultRecyclerAdapter(val context: Context, val itemList: MutableList<MovieItem>): RecyclerView.Adapter<ResultViewHolder>() {
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ResultViewHolder(ItemResultBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = itemList.get(position)

        holder.binding.run {
            movieTitle.text = result.title
            movieSchedule.text = result.schedule.toString()
            movieDirector.text = movieDirector.text.toString() + result.director
            movieRating.text = movieRating.text.toString() + result.rating.toString()
            movieGenre.text = movieGenre.text.toString() + result.genre
        }
    }
}