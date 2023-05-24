import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var contentTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleTextView = findViewById(R.id.titleTextView)
        contentTextView = findViewById(R.id.contentTextView)

        titleTextView.setOnClickListener {
            val isContentVisible = contentTextView.visibility == View.VISIBLE
            if (isContentVisible) {
                contentTextView.visibility = View.GONE
            } else {
                contentTextView.visibility = View.VISIBLE
                val postContent = "공지사항 내용입니다."
                contentTextView.text = postContent
            }
        }
    }
}