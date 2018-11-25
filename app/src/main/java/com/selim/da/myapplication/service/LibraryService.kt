package com.selim.da.myapplication.service

import com.selim.da.myapplication.model.Book
import retrofit2.Call
import retrofit2.http.GET

interface LibraryService {

    @GET("books")
    fun listBooks(): Call<List<Book>>

}