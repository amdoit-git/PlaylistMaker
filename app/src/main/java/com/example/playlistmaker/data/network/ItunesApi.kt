package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.ItunesTrackList
import com.example.playlistmaker.data.dto.ItunesError
import com.example.playlistmaker.data.dto.ItunesTrack
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ItunesApi {

    private val baseUrl: String = "https://itunes.apple.com"

    private val gson: Gson = Gson();

    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build();

    private val api: ItunesApiService = retrofit.create<ItunesApiService>();

    fun search(
        textToSearch: String,
        onSuccess: (List<ItunesTrack>) -> Unit,
        onEmpty: () -> Unit,
        onFail: (String, ItunesError?) -> Unit
    ) {

        api.search(textToSearch).enqueue(object : Callback<ItunesTrackList> {
            override fun onResponse(p0: Call<ItunesTrackList>, p1: Response<ItunesTrackList>) {

                if (p1.isSuccessful) {

                    val body = p1.body()

                    if (body != null && body.results.isNotEmpty()) {

                        onSuccess(body.results);
                    } else {
                        onEmpty();
                    }

                } else {

                    val body = p1.errorBody();

                    onFail(
                        textToSearch,
                        if (body == null) ItunesError("Сервер отклонил наш запрос с ошибкой") else getError(
                            body.string()
                        )
                    );
                }
            }

            override fun onFailure(p0: Call<ItunesTrackList>, p1: Throwable) {
                onFail(textToSearch, ItunesError("Проблема с сетью. Не удалось установить соединение"))
            }

        })
    }

    private fun getError(message: String): ItunesError {

        return try {
            gson.fromJson(message, ItunesError::class.java)
        } catch (er: JsonSyntaxException) {
            ItunesError(message)
        }
    }
}

