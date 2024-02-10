package com.dicoding.movie21.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.movie21.R
import com.dicoding.movie21.core.domain.model.Movie
import com.dicoding.movie21.databinding.ActivityDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel : DetailViewModel by viewModel()


    companion object{
        const val EXTRA_DATA = "EXTRA_DATA"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailMovie = intent.getParcelableExtra<Movie>(EXTRA_DATA)
        showDetailMovie(detailMovie)


    }

    private fun showDetailMovie(detailMovie: Movie?) {
        detailMovie?.let {
            binding.tvDeskripsi.text = detailMovie.overview
            binding.tvTitle.text = detailMovie.title
            binding.tvDate.text = detailMovie.releaseDate
            Glide.with(this@DetailActivity)
                .load("https://image.tmdb.org/t/p/w780/${detailMovie.backdropPath}")
                .into(binding.imgPosterDetail)


            var statusFavorite = detailMovie.isFavorite
            setStatusFavorite(statusFavorite)
            binding.btnFavorite.setOnClickListener {
                statusFavorite = !statusFavorite
                detailViewModel.setFavoriteMovie(detailMovie, statusFavorite)
                setStatusFavorite(statusFavorite)
            }

        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.btnFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_favorite))
        } else {
            binding.btnFavorite.setImageDrawable(ContextCompat.getDrawable(this, com.dicoding.movie21.core.R.drawable.icon_favorite_border))
        }
    }

}