import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import com.example.cinemaisland.BaseActivity
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityDetailBinding

class DetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

        val noticeTitle = intent.getStringExtra("notice_title")
        val noticeContent = intent.getStringExtra("notice_content")

        val titleTextView: TextView = findViewById(R.id.notice_title)
        val contentTextView: TextView = findViewById(R.id.notice_content)

        titleTextView.text = noticeTitle
        contentTextView.text = noticeContent
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_detail
    }
}

