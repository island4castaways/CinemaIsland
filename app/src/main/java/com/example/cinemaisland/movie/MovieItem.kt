package com.example.cinemaisland.movie

import java.io.File

class MovieItem(
        @field:JvmField
        val imageViewPoster:File,
        val movoieDate: String,
        val eventCount: Int,
        val textViewTitle: String
    ) {
}