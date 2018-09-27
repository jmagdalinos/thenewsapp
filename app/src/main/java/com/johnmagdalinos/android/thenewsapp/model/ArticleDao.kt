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

package com.johnmagdalinos.android.thenewsapp.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * DAO used to Insert, Query and Delete the articles from the Database
 */
@Dao
interface ArticleDao {
    // Returns a list of all articles
    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    // Inserts articles
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(vararg articles: Article)

    // Deletes all articles
    @Query("DELETE FROM articles")
    fun deleteAllArticles()
}