package com.example.cinemaisland

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cinemaisland.databinding.ActivityMovieWriteBinding
import com.example.cinemaisland.model.MovieItem
import com.example.cinemaisland.util.stringToDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.Date


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



        //장르 spinner 설정
        val genreAdapter = ArrayAdapter.createFromResource(this, R.array.genre, android.R.layout.simple_spinner_item)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genreSpinner.adapter = genreAdapter

        //갤러리 실행 후 이미지뷰에 이미지 로딩
        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            try{
                // isSampleSize 비율 계산, 지정
                val calRatio = calculateInSampleSize(//적절한 비율로 이미지 크기를 줄이는 함수
                    it!!.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize))
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                //이미지 로딩
                var inputStream = contentResolver.openInputStream(it!!.data!!.data!!)
                bitmap = BitmapFactory.decodeStream(inputStream,null,option)
                inputStream!!.close()   //메모리 해제
                bitmap?.let{
                    binding.imageViewPoster.setImageBitmap(bitmap)    //이미지 뷰
                }?: let{
                    Log.d("kkang", "bitmap null")
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        //이미지 업로드 버튼
        binding.selectPhotoBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type="image/*"
            requestGalleryLauncher.launch(intent)
        }

        //저장 버튼
        binding.saveBtn.setOnClickListener {
            //이미지 외 텍스트
            val title = binding.textViewTitle.text.toString()
            val genre = binding.genreSpinner.selectedItem.toString()
            val director = binding.director.text.toString()
            val content = binding.detailContent.text.toString()
            val pickDate = binding.inputRaffleDate.text.toString()
            val runDate = binding.Schedule.text.toString()
            val date = Timestamp(Date())

            //이미지 관련
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 40, baos)
            val data = baos.toByteArray()
            val storageReference = storage!!.reference

            storageReference.child("moviePoster_$title")
                .putBytes(data).addOnSuccessListener {
                    val result = it.metadata!!.reference!!.downloadUrl
                    result.addOnSuccessListener { uri->
                        val imageLink = uri.toString()

                        val MovieItem = MovieItem()
                        MovieItem.id = "$title@$pickDate"
                        MovieItem.title = title
                        MovieItem.genre = genre
                        MovieItem.director = director
                        MovieItem.details = content
                        MovieItem.raffleDate = stringToDate(pickDate)
                        MovieItem.schedule = stringToDate(runDate)
                        MovieItem.imageUrl=imageLink

                        db.collection("movie")
                            .document("$title@pickDate")
                            .set(MovieItem)
                    }
                }

        }
    }


    override fun getLayoutResId(): Int {
        return R.layout.activity_movie_write
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth:Int, reqHeight:Int):Int{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true   //옵션 설정
        try{
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream,null,options)
            inputStream!!.close()
            inputStream=null
        }catch (e:Exception){
            e.printStackTrace()
        }
        //height와 width 변수 값 설정
        val(height: Int, width: Int) = options.run{outHeight to outWidth}
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if(height > reqHeight || width > reqWidth){
            val halfHeight:Int = height/2
            val halfWidth:Int = width/2
            while(halfHeight/inSampleSize >= reqHeight && halfWidth/inSampleSize >= reqWidth){
                inSampleSize *=2
            }
        }
        return inSampleSize
    }
}
