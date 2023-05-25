import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.R
import com.example.cinemaisland.notification.NoticeItem

class NoticeRecyclerAdapter(private val onItemClick: (NoticeItem) -> Unit) : RecyclerView.Adapter<NoticeRecyclerAdapter.ViewHolder>() {

    private var noticeList: ArrayList<NoticeItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noticeList[position])
    }

    fun setNoticeList(list: ArrayList<NoticeItem>) {
        noticeList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noticeTitle: TextView = itemView.findViewById(R.id.notice_title)
        private val noticeDate: TextView = itemView.findViewById(R.id.notice_date)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = noticeList[position]
                    onItemClick.invoke(item)
                }
            }
        }

        fun bind(item: NoticeItem) {
            noticeTitle.text = item.notice_title
            noticeDate.text = item.notice_date
        }
    }
}
