package vinhtv.android.offlineapp

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.util.SortedListAdapterCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import vinhtv.android.offlineapp.model.FeedItem
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.util.L

/**
 * Created by Admin on 11/1/2017.
 */
class FeedAdapter: RecyclerView.Adapter<FeedAdapter.FeedItemViewHolder>() {

    private val uniqueMapping = HashMap<String, FeedItem>()
    private val list = createSortedList()

    private fun createKeyFor(post: Post) = post.id

    fun insert(item: FeedItem) {
        val key = createKeyFor(item.post)
        val existing = uniqueMapping[key]
        if(existing == null) {
            list.add(item)
        } else {
            val pos = list.indexOf(existing)
            list.updateItemAt(pos, item)
        }
        uniqueMapping.put(key, item)
    }

    fun update(post: Post) {
        val key = createKeyFor(post)
        val existing = uniqueMapping[key]
        if(existing == null) {
            L.d("update post is received but it does not exist, ignoring... $key")
            return
        }
        val pos = list.indexOf(existing)
        val newItem = FeedItem(existing.user, post)
        uniqueMapping.put(key, newItem)
        list.updateItemAt(pos, newItem)
    }

    fun insert(items: List<FeedItem>) {
        items.forEach { insert(it) }
    }

    fun swapList(items: List<FeedItem>) {
        val newListKeys = HashSet<String>()
        items.forEach { newListKeys.add(createKeyFor(it.post)) }

        if(list.size() > 0) {
            // remove redundant items
            for (i in list.size() - 1..0) {
                val item = list[i]
                val key = createKeyFor(item.post)
                if (!newListKeys.contains(key)) {
                    uniqueMapping.remove(key)
                    list.removeItemAt(i)
                }
            }
        }
        insert(items)
    }

    fun remove(post: Post) {
        val item = uniqueMapping.remove(createKeyFor(post))
        if(item != null) list.remove(item)
    }

    override fun getItemCount() = list.size()

    override fun onBindViewHolder(holder: FeedItemViewHolder?, position: Int) {
        val feedItem = list.get(position)
        holder?.tvUser?.text = feedItem.user.name
        holder?.tvMessage?.text = feedItem.post.text
        holder?.ivSync?.visibility = if(feedItem.post.pending) View.VISIBLE else View.INVISIBLE
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FeedItemViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return FeedItemViewHolder(inflater.inflate(R.layout.feed_item, parent, false))
    }

    private fun createSortedList() = SortedList<FeedItem>(FeedItem::class.java, object: SortedListAdapterCallback<FeedItem>(this) {
        override fun compare(o1: FeedItem?, o2: FeedItem?): Int {
            val p1 = o1?.post
            val p2 = o2?.post
            if(p1?.pending != p2?.pending) return if(p1?.pending == true) -1 else 1
            return (p2!!.created.minus(p1!!.created)).toInt()
        }

        override fun areItemsTheSame(item1: FeedItem?, item2: FeedItem?): Boolean {
            return item1?.post?.id == item2?.post?.id
        }

        override fun areContentsTheSame(oldItem: FeedItem?, newItem: FeedItem?): Boolean {
            return oldItem?.post?.same(newItem?.post)?: false
        }
    })

    class FeedItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.post_text)
        val tvUser: TextView = itemView.findViewById(R.id.user_name)
        val ivSync: ImageView = itemView.findViewById(R.id.sync_icon)
    }
}