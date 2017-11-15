package vinhtv.android.offlineapp

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.Toast
import vinhtv.android.offlineapp.model.FeedItem

class FeedActivity : AppCompatActivity() {

    private var editText: EditText? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private val feedAdapter = FeedAdapter()
    private var viewModel: FeedViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        swipeRefreshLayout = findViewById(R.id.swipe_container)
        editText = findViewById(R.id.inputText)

        findViewById<FloatingActionButton>(R.id.fab)
                .setOnClickListener{ sendPost() }
        swipeRefreshLayout!!.setOnRefreshListener { fetchFeedAsync() }
        initRecyclerView()

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel!!.observeData()
        viewModel!!.uiEventObservable.observe(this, uiEventObserver)
        viewModel!!.feedObservable.observe(this, feedObserver)
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = feedAdapter
    }

    private fun sendPost() {
        val message = editText!!.text.toString()
        editText?.setText("")
        if(message.isBlank()) {
            Toast.makeText(this, "empty content will not be sent", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel!!.addFeed(message)
//        feedAdapter.insert(FeedItem(viewModel!!.user, viewModel!!.createPost(message)))
    }

    private fun fetchFeedAsync() {
        viewModel!!.fetchFeeds()
    }

    private val feedObserver = Observer<List<FeedItem>> {
        feedAdapter.swapList(it!!)
    }

    private val uiEventObserver = Observer<UIFeedEvent> {
        when(it) {
            UIFeedEvent.NONE -> {}
            UIFeedEvent.REFRESHED -> { swipeRefreshLayout!!.isRefreshing = false }
        }
    }
}
