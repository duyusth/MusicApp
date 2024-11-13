package com.example.musicapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var searchJob: Job? = null
    private lateinit var songAdapter: SongAdapter
    private lateinit var rvSongs: RecyclerView
    private lateinit var etSearch: EditText
    private val songs = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo RecyclerView
        rvSongs = findViewById(R.id.rvSongs)
        rvSongs.layoutManager = LinearLayoutManager(this)
        songAdapter = SongAdapter()
        rvSongs.adapter = songAdapter

        // Gắn TextWatcher vào EditText
        etSearch = findViewById(R.id.etSearch)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                s?.let {
                    if (it.isNotEmpty()) {
                        searchJob = CoroutineScope(Dispatchers.Main).launch {
                            delay(500) // Debounce 500ms
                            searchSongs(it.toString())
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun searchSongs(query: String) {
        Log.d("SEARCH_SONGS", "Searching for: $query")
        val api = DeezerApiClient.instance
        api.searchTracks(query = query, apiKey = "your-api-key-here").enqueue(object : Callback<DeezerTrackResponse> {
            override fun onResponse(
                call: Call<DeezerTrackResponse>,
                response: Response<DeezerTrackResponse>
            ) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.data ?: emptyList()
                    Log.d("SEARCH_SONGS", "Received tracks: ${tracks.size}")
                    songAdapter.submitList(tracks)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    Log.e("SEARCH_SONGS", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<DeezerTrackResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to connect to server", Toast.LENGTH_SHORT).show()
                Log.e("SEARCH_SONGS", "API Call Failed: ${t.message}", t)
            }
        })
    }

    override fun onDestroy() {
        searchJob?.cancel()
        super.onDestroy()
    }
}
