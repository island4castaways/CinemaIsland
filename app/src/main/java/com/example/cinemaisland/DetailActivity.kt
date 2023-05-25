import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.cinemaisland.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val noticeTitle = intent.getStringExtra("notice_title")
        val noticeContent = intent.getStringExtra("notice_content")

        val titleTextView: TextView = findViewById(R.id.notice_title)
        val contentTextView: TextView = findViewById(R.id.notice_content)

        titleTextView.text = noticeTitle
        contentTextView.text = noticeContent
    }
}
