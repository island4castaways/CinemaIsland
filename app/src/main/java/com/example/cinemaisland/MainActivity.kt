import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.NoticeItem
import com.example.cinemaisland.R
import com.example.cinemaisland.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        noticeAdapter = NoticeAdapter()
        recyclerView.adapter = noticeAdapter

        // 공지사항 데이터 리스트 생성
        val noticeList = createNoticeList()
        noticeAdapter.submitList(noticeList)
    }

    private fun createNoticeList(): List<NoticeItem> {
        val noticeList = mutableListOf<NoticeItem>()
        // 여기에 공지사항 데이터를 생성하거나 가져오는 로직을 구현합니다.
        // 예시로 몇 가지 하드코딩된 공지사항 아이템을 추가합니다.
        noticeList.add(NoticeItem("공지사항 제목 1", "공지사항 내용 1"))
        noticeList.add(NoticeItem("공지사항 제목 2", "공지사항 내용 2"))
        noticeList.add(NoticeItem("공지사항 제목 3", "공지사항 내용 3"))
        return noticeList
    }
}