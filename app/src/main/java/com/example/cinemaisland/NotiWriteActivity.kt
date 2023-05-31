package com.example.cinemaisland

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.example.cinemaisland.databinding.ActivityNotiWriteBinding
import com.example.cinemaisland.notice.NoticeItem
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date


class NotiWriteActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNotiWriteBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)
        var db: FirebaseFirestore = FirebaseFirestore.getInstance()

        binding.saveBtn.setOnClickListener {
            val title = binding.inputTitle.text.toString()
            val content = binding.inputContent.text.toString()
            val date =  Timestamp(Date())

            val noti = NoticeItem()
            noti.title = title
            noti.content=content
            noti.date=date
            noti.id = "$title"


            db.collection("notice")
                .document("$title")
                .set(noti)

            val intent = Intent(this, BoardActivity::class.java)
            startActivity(intent)
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_noti_write
    }
}