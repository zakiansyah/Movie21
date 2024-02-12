package com.dicoding.movie21.home

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.movie21.R
import com.dicoding.movie21.core.ui.MovieAdapter
import com.dicoding.movie21.databinding.ActivityHomeBinding
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.dicoding.movie21.core.data.source.Resource
import com.dicoding.movie21.detail.DetailActivity
import com.dicoding.movie21.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding

    private val homeViewModel : HomeViewModel by viewModel()

    private val movieAdapter = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, selectedData)
            startActivity(intent)
        }

        homeViewModel.movie.observe(this) { movie ->
            Log.d("HomePage", "Data observed: ${Gson().toJson(movie)}")
            if (movie != null) {
                when (movie) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        movieAdapter.submitList(movie.data)

                        Log.d("HomePage", "Data loaded successfully: ${movie.data}")
                        Log.d("HomePage", "Full Response: ${Gson().toJson(movie)}")
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        Log.d("HomePage", "Loading data...")
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("HomePage", "Error loading data: ${movie.message}")
                    }
                }
            }
        }

        with(binding.rvHome) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_favorite -> {
                val uri = Uri.parse("movie21://favorite")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
                true
            }
            R.id.btn_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

