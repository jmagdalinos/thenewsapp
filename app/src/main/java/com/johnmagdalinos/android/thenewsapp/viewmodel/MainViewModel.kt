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

package com.johnmagdalinos.android.thenewsapp.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.johnmagdalinos.android.thenewsapp.model.Article
import com.johnmagdalinos.android.thenewsapp.Repository

/**
 * ViewModel used with the MainFragment.
 */
class MainViewModel(private val repository: Repository): ViewModel() {
    private var articleList: LiveData<List<Article>>? = null

    fun init(shouldUpdate: Boolean) {
        articleList = repository.getArticles(shouldUpdate)
    }

    fun getAllArticles(): LiveData<List<Article>>? = articleList
}