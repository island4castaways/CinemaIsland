import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.R
import com.example.cinemaisland.notification.NoticeItem

class MainActivity : AppCompatActivity() {

    private lateinit var nRecyclerView: RecyclerView
    private lateinit var nRecyclerAdapter: NoticeRecyclerAdapter
    private lateinit var noticeItems: ArrayList<NoticeItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //공지사항 리사이클러 뷰
        nRecyclerView = findViewById(R.id.recyclerView)

        /* initiate adapter */
        nRecyclerAdapter = NoticeRecyclerAdapter { item ->
            // 클릭 시 다른 화면으로 이동하고 제목과 본문을 전달
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("notice_title", item.notice_title)
            intent.putExtra("notice_content", item.notice_content)
            startActivity(intent)
        }

        /* initiate recyclerview */
        nRecyclerView.adapter = nRecyclerAdapter
        nRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        //공지사항 간격을 줄이고싶은데 모르겠...도와주십...사 ㅜㅜ
//        /* set item spacing */
//        val itemSpacing = resources.getDimensionPixelSize(R.dimen.item_spacing)
//        nRecyclerView.addItemDecoration(RecyclerView.ItemDecoration(itemSpacing))

        /* adapt data */
        noticeItems = ArrayList()
        for (i in 1..10) {
            val noticeContent = "본문 내용 $i"
            noticeItems.add(NoticeItem("id:$i", "$i 번째 제목", "날짜", noticeContent))
        }
        nRecyclerAdapter.setNoticeList(noticeItems)
    }
}
