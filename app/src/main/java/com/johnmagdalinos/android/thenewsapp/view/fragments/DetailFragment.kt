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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.johnmagdalinos.android.thenewsapp.R
import com.johnmagdalinos.android.thenewsapp.utils.Constants.Companion.DETAIL_ACTIVITY_URL_STRING
import kotlinx.android.synthetic.main.fragment_detail.view.*

/**
 * Fragment hosting a WebView. Shows a single article through its URL
 */
class DetailFragment : Fragment() {
    private var mWebView: WebView? = null
    private var progressBar: ProgressBar? = null

    companion object {
        fun newInstance(urlString: String): DetailFragment {
            val fragment: DetailFragment = DetailFragment()
            val args: Bundle = Bundle()
            args.putString(DETAIL_ACTIVITY_URL_STRING, urlString)
            fragment.arguments = args
            return fragment
        }
    }

    /** Create the view and setup the WebView */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_detail, container, false)

        // ProgressBar used to indicate loading of data
        progressBar = view.pb_detail

        // Setup the WebView
        mWebView = view.webview
        with(mWebView!!) {
            // Load the article's URL
            loadUrl(arguments?.getString(DETAIL_ACTIVITY_URL_STRING))
            // Instantiate a WebViewClient with a listener hiding the ProgressBar when page loading
            // is complete
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    progressBar?.visibility = View.INVISIBLE
                    super.onPageFinished(view, url)
                }
            }
        }
        return view
    }

    /** Navigates back from the webpage loaded into the WebView */
    fun goBack() {
        if (mWebView != null && mWebView!!.canGoBack()) mWebView!!.goBack()
    }
}
