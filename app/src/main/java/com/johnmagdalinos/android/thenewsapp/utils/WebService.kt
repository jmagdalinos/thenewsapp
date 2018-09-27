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

package com.johnmagdalinos.android.thenewsapp.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.johnmagdalinos.android.thenewsapp.model.ApiJsonObject
import com.johnmagdalinos.android.thenewsapp.utils.Constants.Companion.API_KEY_QUERY
import com.johnmagdalinos.android.thenewsapp.utils.Constants.Companion.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Helper instantiating Retrofit and GSON in order to get a JSON response from the REST API and
 * convert it toArticle POJOs
 */
interface WebService {
    @GET("svc/mostpopular/v2/mostviewed/{section}/{period}.json")
    fun loadArticles (
            @Path("section") section: String,
            @Path("period") period: String,
            @Query(API_KEY_QUERY) apiKey: String): Call<ApiJsonObject>

    /**
     * Companion object to create the webService
     */
    companion object Factory {
        fun create() : WebService {

            val gson: Gson = GsonBuilder()
                    .setLenient()
                    .create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(WebService::class.java)
        }
    }
}