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

package com.johnmagdalinos.android.thenewsapp

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.johnmagdalinos.android.thenewsapp.model.ApiJsonObject
import com.johnmagdalinos.android.thenewsapp.model.Article
import com.johnmagdalinos.android.thenewsapp.model.ArticleDao
import com.johnmagdalinos.android.thenewsapp.utils.AppExecutors
import com.johnmagdalinos.android.thenewsapp.utils.Constants
import com.johnmagdalinos.android.thenewsapp.utils.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository class used to communicate between the ViewModel and the Model
 * Contains the logic for either downloading the data from the REST API or retrieving it from the
 * Database
 */
class Repository {
    companion object {
        // DAOs used to perform actions on the Database
        private lateinit var articleDao: ArticleDao
        private lateinit var appExecutors: AppExecutors
        private lateinit var webService: WebService

        fun newInstance(articleDao: ArticleDao, appExecutors: AppExecutors, webService: WebService): Repository {
            val sr = Repository()
            synchronized(sr) {
                Companion.articleDao = articleDao
                Companion.appExecutors = appExecutors
                Companion.webService = webService
            }
            return sr
        }
    }

    /** Retrieves all the articles from the database */
    fun getArticles(shouldUpdate: Boolean): LiveData<List<Article>> {
        // Get the API Key
        val apiKey: String = BuildConfig.APIKEY
        // If the articles are old or the user requested a refresh delete the stored articles
        if (shouldUpdate) {
            // Delete all articles
            deleteArticles()
        } else {
            val articlesFromDb = articleDao.getAllArticles()

            if (articlesFromDb.value != null) return articlesFromDb
        }

        val data : MutableLiveData<List<Article>> = MutableLiveData()
        appExecutors.networkIO().execute {
            webService.loadArticles(Constants.ALL_SECTIONS, "7", apiKey).enqueue(object:
                    Callback<ApiJsonObject> {
                override fun onFailure(call: Call<ApiJsonObject>, t: Throwable) {

                }

                override fun onResponse(call: Call<ApiJsonObject>, response: Response<ApiJsonObject>) {
                    // Read the JSON response and insert the downloaded articles into the Database
                    val jsonObject: ApiJsonObject = response.body() ?: return

                    data.value = jsonObject.results

                    if (data.value != null){
                        appExecutors.diskIO().execute {
                            articleDao.insertArticles(*(data.value)!!.toTypedArray())
                        }
                    }
                }
            })
        }
        return data
    }

    /** Deletes all the articles in the database */
    private fun deleteArticles() = appExecutors.diskIO().execute{ articleDao.deleteAllArticles() }
}