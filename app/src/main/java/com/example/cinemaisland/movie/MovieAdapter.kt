package com.example.cinemaisland.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemaisland.R
import com.example.cinemaisland.model.MovieItem

class MovieAdapter(private var movieList: List<MovieItem>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun updateData(newList: List<MovieItem>) {
        movieList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewPoster: ImageView = itemView.findViewById(R.id.imageViewPoster)
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)

        fun bind(movie: MovieItem) {
            // Set the movie title
            textViewTitle.text = movie.title

            // Set the poster image (replace placeholder_poster with the actual image)
            Glide.with(itemView)
                .load(movie.imageUrl)
                .into(imageViewPoster)
        }
    }
}
