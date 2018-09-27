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

import android.content.Context
import com.johnmagdalinos.android.thenewsapp.Repository
import com.johnmagdalinos.android.thenewsapp.model.ArticleDatabase
import com.johnmagdalinos.android.thenewsapp.viewmodel.MainViewModelFactory

/**
 * Provides instances of the Repository and ViewModelFactory for any classes that need them
 */
class InjectorUtils {
    /** Provides a new or an existing instance of the Repository */
    fun provideRepository(context: Context) : Repository {
        val articleDatabase = ArticleDatabase.newInstance(context)
        val appExecutors = AppExecutors.getInstance()
        val webService = WebService.create()
        return Repository.newInstance(articleDatabase!!.articleDao(), appExecutors, webService)
    }

    /** Provides a new or an existing instance of the ViewModelFactory */
    fun provideViewModelFactory(context: Context) : MainViewModelFactory {
        val repository = provideRepository(context)
        return MainViewModelFactory(repository)
    }
}