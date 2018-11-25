package com.selim.da.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.selim.da.myapplication.fragments.DetailFragment
import com.selim.da.myapplication.fragments.ListFragment
import com.selim.da.myapplication.model.Book


class MainActivity : AppCompatActivity(), ListFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener2 {

    // yes we can retrieve a fragment through the supportFragmentManager.findFragmentBytag but it simpler like that
    private var currentDetailFragment: Fragment? = null

    override fun onCloseFragment() {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
                .replace(R.id.list_fragment, ListFragment(), ListFragment::class.java.name).addToBackStack("list_fragment")
                .commit()
    }

    override fun onSelectBook(book: Book) {

        val detailFragment = DetailFragment.newInstance(book, resources.configuration.orientation == 2)
        currentDetailFragment = detailFragment
        if(resources.configuration.orientation == 1) {
            supportFragmentManager.popBackStack("list_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment, detailFragment, DetailFragment::class.java.name).addToBackStack("detail_fragment")
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .replace(R.id.list_fragment, ListFragment(), ListFragment::class.java.name).addToBackStack("list_fragment")
                .commit()
        if(resources.configuration.orientation == 2){
            supportFragmentManager.beginTransaction()
                    .replace(R.id.detail_fragment, DetailFragment(), DetailFragment::class.java.name).addToBackStack("detail_fragment")
                    .commit()
        }else{
            supportFragmentManager.popBackStack("detail_fragment" , FragmentManager.POP_BACK_STACK_INCLUSIVE)
            // support to destroy the DetailFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if(currentDetailFragment != null) {
            supportFragmentManager.putFragment(outState!!, "savedDetailFragment", currentDetailFragment!!);
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        val frag = supportFragmentManager.getFragment(savedInstanceState!!, "savedDetailFragment")
        if(frag != null){
            if(resources.configuration.orientation == 2) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.detail_fragment, frag, DetailFragment::class.java.name).addToBackStack("detail_fragment")
                        .commit()
            }else {
                supportFragmentManager.popBackStack("detail_fragment" , FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }
}