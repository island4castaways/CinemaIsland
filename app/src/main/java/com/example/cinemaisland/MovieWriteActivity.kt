package com.example.cinemaisland

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.cinemaisland.databinding.ActivityMovieWriteBinding
import com.example.cinemaisland.model.MovieItem
import com.example.cinemaisland.util.dateTimeToString
import com.example.cinemaisland.util.dateToString
import com.example.cinemaisland.util.stringToDate
import com.example.cinemaisland.util.timeStringToDate
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class MovieWriteActivity : BaseActivity() {
    var storage: FirebaseStorage? = null
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMovieWriteBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

        val baos = ByteArrayOutputStream()
        var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        val genreArray = resources.getStringArray(R.array.genre)
        val ratingArray = resources.getStringArray(R.array.rating)

        //장르 spinner 설정
        val genreAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.genre,
            android.R.layout.simple_spinner_item
        )
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genreSpinner.adapter = genreAdapter

        val ratingAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.rating,
            android.R.layout.simple_spinner_item
        )
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.movieRatingSpinner.adapter = ratingAdapter


        val intentMovieItem = intent.getSerializableExtra("movieItem") as? MovieItem
        if (intentMovieItem != null) {
            Glide.with(binding.imageViewPoster.context).load(intentMovieItem.imageUrl)
                .into(binding.imageViewPoster)
            val genreIndex = genreArray.indexOf(intentMovieItem.genre)
            Log.d("ssum", "$genreIndex")
            binding.genreSpinner.setSelection(genreIndex)
            binding.detailContent.setText(intentMovieItem.details)
            binding.director.setText(intentMovieItem.director)
            binding.director.isEnabled = false
            binding.textViewTitle.setText(intentMovieItem.title)
            binding.textViewTitle.isEnabled =false
            binding.inputRaffleDate.setText(dateToString(intentMovieItem.raffleDate!!))
            binding.Schedule.setText(dateTimeToString(intentMovieItem.schedule!!))
            binding.selectPhotoBtn.visibility= View.GONE
            val ratingIndex = ratingArray.indexOf(intentMovieItem.rating.toString())
            Log.d("ssum", "$ratingIndex")
            binding.movieRatingSpinner.setSelection(ratingIndex)
        }


        //갤러리 실행 후 이미지뷰에 이미지 로딩
        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            try {
                // isSampleSize 비율 계산, 지정
                val calRatio = calculateInSampleSize(//적절한 비율로 이미지 크기를 줄이는 함수
                    it!!.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                //이미지 로딩
                var inputStream = contentResolver.openInputStream(it!!.data!!.data!!)
                bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()   //메모리 해제
                bitmap?.let {
                    binding.imageViewPoster.setImageBitmap(bitmap)    //이미지 뷰
                } ?: let {
                    Log.d("kkang", "bitmap null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //이미지 업로드 버튼
        binding.selectPhotoBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        //저장 버튼
        binding.saveBtn.setOnClickListener {
            //이미지 외 텍스트
            val title = binding.textViewTitle.text.toString()
            val genre = binding.genreSpinner.selectedItem.toString()
            val director = binding.director.text.toString()
            val content = binding.detailContent.text.toString()
            val raffleDate = stringToDate(binding.inputRaffleDate.text.toString())
            val schedule = timeStringToDate(binding.Schedule.text.toString())
            val rating = binding.movieRatingSpinner.selectedItem.toString().toInt()

            if (intentMovieItem != null) {//수정일 때
                val updatedMovieItem = hashMapOf(
                    "genre" to genre,
                    "details" to content,
                    "raffleDate" to raffleDate,
                    "schedule" to schedule,
                    "rating" to rating,
                )

                db.collection("movie")
                    .document("$title@$director")
                    .set(updatedMovieItem, SetOptions.merge())
                    .addOnSuccessListener {
                        // 업데이트 성공 시 수행할 작업
                        val intent = Intent(this, BoardActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "수정에 실패했습니다.",  Toast.LENGTH_SHORT).show()
                    }
            } else {//등록일 때
                //이미지 관련
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 40, baos)
                val data = baos.toByteArray()
                val storageReference = storage!!.reference

                storageReference.child("moviePoster_$title")
                    .putBytes(data).addOnSuccessListener {
                        val result = it.metadata!!.reference!!.downloadUrl
                        result.addOnSuccessListener { uri ->
                            val imageLink = uri.toString()

                            val MovieItem = MovieItem()
                            MovieItem.id = "$title@$director"
                            MovieItem.title = title
                            MovieItem.genre = genre
                            MovieItem.director = director
                            MovieItem.details = content
                            MovieItem.raffleDate = raffleDate
                            MovieItem.schedule = schedule
                            MovieItem.imageUrl = imageLink
                            MovieItem.rating=rating

                            db.collection("movie")
                                .document("$title@$director")
                                .set(MovieItem).addOnSuccessListener {
                                    val intent = Intent(this, BoardActivity::class.java)
                                    startActivity(intent)
                                }
                        }.addOnFailureListener {
                            Toast.makeText(this, "등록에 실패했습니다.",  Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_movie_write
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true   //옵션 설정
        try {
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //height와 width 변수 값 설정
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
