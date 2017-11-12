package vinhtv.android.offlineapp

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

class FeedActivity : AppCompatActivity() {

    private val feedAdapter = FeedAdapter()
    private var editText: EditText? = null

    private var viewModel: FeedViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        findViewById<FloatingActionButton>(R.id.fab)
                .setOnClickListener{ sendPost() }
        findViewById<SwipeRefreshLayout>(R.id.swipe_container)
                .setOnRefreshListener { fetchFeedAsync() }
        initRecyclerView()
        editText = findViewById(R.id.inputText)

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
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
//        feedAdapter.insert(FeedItem(viewModel!!.user, viewModel!!.createPost(message)))
    }

    private fun fetchFeedAsync() {}
}
