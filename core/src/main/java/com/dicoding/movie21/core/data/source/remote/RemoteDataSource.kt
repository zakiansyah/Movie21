package com.dicoding.movie21.core.data.source.remote

import android.util.Log
import com.dicoding.movie21.core.data.source.remote.network.ApiService
import com.dicoding.movie21.core.data.source.remote.network.Result
import com.dicoding.movie21.core.data.source.remote.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource (private val apiService: ApiService){

    suspend fun getAllMovie(): Flow<Result<List<MovieResponse>>> {
        return flow {
            try {
                val response = apiService.getAllMovie()
                val data = response.results
                if (data.isNotEmpty()) {
                    emit(Result.Success(response.results))
                    Log.d("RemoteDataSource", "Data loaded successfully: $data")
                } else {
                    emit(Result.Loading)
                    Log.d("RemoteDataSource", "Data is empty")
                }
            }catch (e:Exception){
                emit(Result.Error(e.toString()))
                Log.e("RemoteDataSource", "Error loading data: ${e.message}")
            }
        }.flowOn(Dispatchers.IO)
    }

}