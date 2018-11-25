package com.selim.da.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
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

    // TODO: Rename method, update argument and hook method into UI event
    fun onItemPressed(book: Book) {
        Log.i("click", "clickfragment")
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onSelectBook(book: Book)
    }

}