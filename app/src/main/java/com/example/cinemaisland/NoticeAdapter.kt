import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaisland.NoticeItem
import com.example.cinemaisland.R

class NoticeAdapter : ListAdapter<NoticeItem, NoticeAdapter.NoticeViewHolder>(NoticeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notice, parent, false)
        return NoticeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class NoticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

        fun bind(noticeItem: NoticeItem) {
            titleTextView.text = noticeItem.title
            contentTextView.text = noticeItem.content
        }
    }

    class NoticeDiffCallback : DiffUtil.ItemCallback<NoticeItem>() {
        override fun areItemsTheSame(oldItem: NoticeItem, newItem: NoticeItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NoticeItem, newItem: NoticeItem): Boolean {
            return oldItem == newItem
        }
    }

}