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

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.johnmagdalinos.android.thenewsapp.R
import com.johnmagdalinos.android.thenewsapp.utils.Constants.Companion.DETAIL_ACTIVITY_URL_STRING
import com.johnmagdalinos.android.thenewsapp.utils.Constants.Companion.MAIN_ACTIVITY_FRAGMENT_STATE
import com.johnmagdalinos.android.thenewsapp.view.fragments.MainFragment

/**
 * Activity containing the MainFragment with the Article RecyclerView
 */
class MainActivity : AppCompatActivity(), MainFragment.MainFragmentCallback {
    private var mFragment: MainFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if there is a saved Fragment
        if (savedInstanceState != null) mFragment = supportFragmentManager.getFragment(savedInstanceState, MAIN_ACTIVITY_FRAGMENT_STATE) as MainFragment?

        setupFragment()
    }

    /** Save the fragment */
    override fun onSaveInstanceState(outState: Bundle) {
        if (mFragment != null) {
            supportFragmentManager.putFragment(outState, MAIN_ACTIVITY_FRAGMENT_STATE, mFragment!!)
        }
        super.onSaveInstanceState(outState)
    }

    /** Setup the fragment */
    private fun setupFragment() {
        // If the Fragment does not exist, create it
        if (mFragment == null) {
            mFragment = MainFragment.newInstance()
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.cl_main, mFragment!!)
                .commit()
    }

    /** Receive a String with the URL from the clicked article */
    override fun onMainFragmentCallback(urlString: String?) {
        val intent: Intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DETAIL_ACTIVITY_URL_STRING, urlString)
        startActivity(intent)

        // Use transitions between Activities
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}
