package com.dicoding.movie21.core.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.movie21.core.databinding.ItemHomeBinding
import com.dicoding.movie21.core.domain.model.Movie
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val movieList = ArrayList<Movie>()
    var onItemClick: ((Movie) -> Unit)? = null


    fun setAllMovieList(movieResponse: List<Movie>?) {
        if (movieResponse != null) {
            movieList.clear()
            movieList.addAll(movieResponse)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var loadImageTask: LoadImageTask? = null

        fun bind(allMovie: Movie) {
            with(binding) {
                tvItemTitle.text = allMovie.title
                tvItemDate.text = allMovie.releaseDate

                val imageUrl = "https://image.tmdb.org/t/p/w185/${allMovie.posterPath}"
                loadImageTask?.cancel(true)
                loadImageTask = LoadImageTask(imgPoster)
                loadImageTask?.execute(imageUrl)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(movieList[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = movieList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = movieList.size

    private class LoadImageTask(private val imageView: ImageView) :
        AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            try {
                val url = URL(params[0])
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                return BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                imageView.setImageBitmap(result)
            }
        }
    }
}