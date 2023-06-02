package com.example.cinemaisland

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemaisland.databinding.EventRecyclerviewBinding
import com.example.cinemaisland.databinding.FragmentOngoingEventBinding
import com.example.cinemaisland.model.MovieItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EndEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EndEventFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOngoingEventBinding.inflate(inflater, container, false)
        val movieList: MutableList<MovieItem> = mutableListOf()
        var db: FirebaseFirestore = FirebaseFirestore.getInstance()

        db.collection("movie")
            .orderBy("raffleDate", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task->
                for(document in task.result){
                    if(document.toObject(MovieItem::class.java).raffleDate!! <= Date(System.currentTimeMillis())){
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