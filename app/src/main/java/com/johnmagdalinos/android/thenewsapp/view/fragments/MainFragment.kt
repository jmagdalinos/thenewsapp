/*
* Copyright (C) 2018 John Magdalinos
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.johnmagdalinos.android.thenewsapp.view.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.johnmagdalinos.android.thenewsapp.R
import com.johnmagdalinos.android.thenewsapp.utils.Constants
import com.johnmagdalinos.android.thenewsapp.utils.Constants.Companion.PREFERENCES_TIME
import com.johnmagdalinos.android.thenewsapp.utils.InjectorUtils
import com.johnmagdalinos.android.thenewsapp.view.ArticleItemDecoration
import com.johnmagdalinos.android.thenewsapp.view.adapters.MainAdapter
import com.johnmagdalinos.android.thenewsapp.viewmodel.MainViewModel
import com.johnmagdalinos.android.thenewsapp.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.util.concurrent.TimeUnit


/**
 * Fragment containing a RecyclerView loaded with all the articles downloaded from the REST API
 */
class MainFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: MainAdapter
    private var recyclerViewState: Parcelable? = null
    private lateinit var callBack: MainFragmentCallback
    private var swipeLayout: SwipeRefreshLayout? = null

    /** Instantiate the ViewModel */
    private val viewModel: MainViewModel by lazy {
        val factory: MainViewModelFactory = InjectorUtils().provideViewModelFactory(context!!)
        ViewModelProviders.of(this.activity!!, factory).get(MainViewModel::class.java)
    }
    private var textView: TextView? = null
    private var progressBar: ProgressBar? = null

    /** Interface used to pass the clicked position to the activity */
    interface MainFragmentCallback {
        fun onMainFragmentCallback(urlString: String?)
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    /** Verify that parent activity implements the callback interface */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            callBack = context as MainFragmentCallback
        } catch (e: ClassCastException) {
            throw ClassCastException("${context?.toString()} must implement MainFragmentCallback")
        }
    }

    /** Initiate the LiveData Observer */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObserver()
    }

    /** Create the view and setup the RecyclerView */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // TextView used to display messages to the user
        textView = view.tv_no_articles
        // ProgressBar used to indicate loading of data
        progressBar = view.pb_main

        // Setup the swipe to refresh
        swipeLayout = view.sr_main
        swipeLayout?.setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.colorAccent))
        swipeLayout?.setOnRefreshListener {
            // Clear the adapter
            recyclerAdapter.submitList(null)
            // Remove All Observers
            viewModel.getAllArticles()?.removeObservers(this.activity!!)
            // Reset the Observer
            setupObserver()
        }

        recyclerViewState = savedInstanceState?.getParcelable(Constants.MAIN_FRAGMENT_RECYCLER_STATE)

        // Setup the recycler adapter
        recyclerAdapter = MainAdapter(context!!) { position: Int ->
            // Pass the clicked item's url to the callback
            val urlString: String? = recyclerAdapter.getItemAtPosition(position)?.url
            callBack.onMainFragmentCallback(urlString)
            recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        }

        // Get the drawable for the recycler view item decoration
        val drawable: Drawable = ContextCompat.getDrawable(context!!, R.drawable.divider_drawable)!!

        // Setup the recycler view
        recyclerView = view.rv_main.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = recyclerAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(ArticleItemDecoration(drawable))
        }
        return view
    }

    /** Setup the LiveData Observer and check for connectivity */
    private fun setupObserver() {
        // Check if there is internet connectivity
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        if (!isConnected) {
            if (swipeLayout!= null && swipeLayout!!.isRefreshing) {
                // Stop the refreshing icon
                swipeLayout!!.isRefreshing = false
                pb_main.visibility = View.INVISIBLE

                // Display notification
                textView?.text = activity!!.getString(R.string.text_view_no_internet)
                textView?.visibility = View.VISIBLE
            }

            if (shouldUpdate(true)) {
                // Display notification
                textView?.text = activity!!.getString(R.string.text_view_no_internet)
                textView?.visibility = View.VISIBLE
                pb_main.visibility = View.INVISIBLE
            }
        }

        // Initiate the Livedata Observer requesting a refresh of the REST API data
        viewModel.init(shouldUpdate(true))

        viewModel.getAllArticles()?.observe(this.activity!!, Observer {articles ->
            recyclerAdapter.submitList(articles)

            if (articles != null && articles.isNotEmpty()) {
                // The news have been fetched. Save the current date in the preferences
                shouldUpdate(false)
                textView?.visibility = View.INVISIBLE
            } else {
                textView?.text = activity!!.getString(R.string.text_view_no_articles)
                textView?.visibility = View.VISIBLE
            }

            // Stop the refreshing icon
            swipeLayout?.isRefreshing = false
            pb_main.visibility = View.INVISIBLE

            // Restore the Recycler View state
            if (recyclerViewState != null) recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            recyclerViewState = null
        })
    }

    /** Used to save or retrieve the date to/from the preferences and return a boolean stating
     * whether the stored articles in the database should be deleted */
    private fun shouldUpdate(get: Boolean): Boolean {
        val prefs: SharedPreferences? = activity?.getPreferences(Context.MODE_PRIVATE)

        // The articles should be deleted every 2 days
        val updateInterval: Long = TimeUnit.DAYS.toMillis(2)
        val currentDate: Long = System.currentTimeMillis()

        return if (get) {
            // Save the current time
            val storedDate: Long = prefs?.getLong(PREFERENCES_TIME, 0) ?: 0
            val dateDiff: Long = currentDate - storedDate
            // Return true only if 2 or more days have passed to notify that the stored articles
            // should be deleted
            dateDiff >= updateInterval
        } else {
            prefs?.edit()
                    ?.putLong(PREFERENCES_TIME, currentDate)
                    ?.apply()
            false
        }
    }

    /** Save the original state of the RecyclerView */
    override fun onSaveInstanceState(outState: Bundle) {
        if (recyclerViewState == null) {
            outState.putParcelable(Constants.MAIN_FRAGMENT_RECYCLER_STATE, recyclerView
                    .layoutManager?.onSaveInstanceState())
        } else {
            outState.putParcelable(Constants.MAIN_FRAGMENT_RECYCLER_STATE, recyclerViewState)
        }

        super.onSaveInstanceState(outState)
    }
}