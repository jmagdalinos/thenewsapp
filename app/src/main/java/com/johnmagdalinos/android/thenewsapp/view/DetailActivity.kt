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

package com.johnmagdalinos.android.thenewsapp.view

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.johnmagdalinos.android.thenewsapp.R
import com.johnmagdalinos.android.thenewsapp.utils.Constants
import com.johnmagdalinos.android.thenewsapp.utils.Constants.Companion.DETAIL_ACTIVITY_URL_STRING
import com.johnmagdalinos.android.thenewsapp.view.fragments.DetailFragment
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * Activity containing the DetailFragment with the WebView
 */
class DetailActivity : AppCompatActivity() {
    private var mFragment: DetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Setup the back button on the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupFragment()
    }

    /** Save the fragment */
    override fun onSaveInstanceState(outState: Bundle) {
        if (mFragment != null) {
            supportFragmentManager.putFragment(outState, Constants.MAIN_ACTIVITY_FRAGMENT_STATE, mFragment!!)
        }
        super.onSaveInstanceState(outState)
    }

    /** Setup the fragment */
    private fun setupFragment() {
        // If the Fragment does not exist, create it
        if (mFragment == null) {
            val urlString: String = intent.getStringExtra(DETAIL_ACTIVITY_URL_STRING)
            mFragment = DetailFragment.newInstance(urlString)
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.cl_detail, mFragment!!)
                .commit()
    }

    /**
     * Setup the menu
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            // Check if the WebView can navigate back
            if (mFragment != null && mFragment!!.webview.canGoBack()) {
                // Navigate back in the WebView
                mFragment!!.goBack()
                return true
            }
            NavUtils.navigateUpFromSameTask(this)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * When the back button is pressed, check if the contained WebView can navigate to a previous
     * webpage. If not, return to the MainActivity
     */
    override fun onBackPressed() {
        // Check if the WebView can navigate back
        if (mFragment != null && mFragment!!.webview.canGoBack()) {
            // Navigate back in the WebView
            mFragment!!.goBack()
        } else {
            super.onBackPressed()
            // Use transitions between Activities
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}