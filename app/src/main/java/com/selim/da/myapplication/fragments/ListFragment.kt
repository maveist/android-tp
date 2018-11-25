package com.selim.da.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.selim.da.myapplication.R
import com.selim.da.myapplication.fragments.adapter.MyBookViewHolder
import com.selim.da.myapplication.model.Book
import com.selim.da.myapplication.service.LibraryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


class ListFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var listener: OnFragmentInteractionListener? = null
    private var books: List<Book>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val retrofit = Retrofit.Builder()
                .baseUrl("http://henri-potier.xebia.fr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create(LibraryService::class.java)
        var books: List<Book>? = null
        val fragment = this
        val theContext = context
        api.listBooks().enqueue(object: Callback<List<Book>> {
            override fun onFailure(call: Call<List<Book>>, t: Throwable){
                Timber.e("error on download data")
            }

            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>){
                books = response.body()
                val recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(fragment.context)
                recyclerView.adapter =
                        MyBookViewHolder(theContext!!,
                                books!!,
                                { book -> onItemPressed(book) })
            }
        })
        return view
    }

    fun onItemPressed(book: Book) {
        listener?.onSelectBook(book)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun onSelectBook(book: Book)
    }

}