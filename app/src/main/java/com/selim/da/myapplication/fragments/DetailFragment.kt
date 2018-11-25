package com.selim.da.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.selim.da.myapplication.R
import com.selim.da.myapplication.model.Book
import com.squareup.picasso.Picasso


class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var book: Book? = null
    private var isLandscape: Boolean = false
    private var listener: OnFragmentInteractionListener2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book = it.getParcelable("book")
            isLandscape = it.getBoolean("isLandscape")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val titleView = view.findViewById<TextView>(R.id.detail_bookTitle)
        val coverView = view.findViewById<ImageView>(R.id.detail_bookCover)
        val synopsisView = view.findViewById<TextView>(R.id.detail_bookSynopsis)

        if(book != null) {
            titleView.text = book!!.title
            Picasso.get().load(book!!.cover).into(coverView)
            var synopsis = ""
            for (part in book!!.synopsis) {
                synopsis += part + "\n"
            }
            synopsisView.text = synopsis
        }else{
            synopsisView.visibility = View.INVISIBLE
            coverView.visibility = View.INVISIBLE
            titleView.text = context!!.getString(R.string.aucun_livre)
        }
        val buttonClose = view.findViewById<ImageView>(R.id.detail_closeButton)
        if(context!!.resources.configuration.orientation == 2){
            buttonClose.visibility = View.INVISIBLE
            synopsisView.setEms(15)

        }else{
            synopsisView.setEms(21)
            buttonClose.setOnClickListener {
                listener!!.onCloseFragment()
            }
        }

        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener2) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


    interface OnFragmentInteractionListener2 {

        fun onCloseFragment()
    }

    companion object {

        @JvmStatic
        fun newInstance(book: Book, isLandscape: Boolean) =
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("book", book)
                        putBoolean("isLandscape", isLandscape)
                    }
                }
    }
}
