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

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Database populated by Article POJOs
 */
@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao() : ArticleDao

    companion object {
        private const val DATABASE_NAME: String = "articles_db"
        private var INSTANCE: ArticleDatabase? = null

        fun newInstance(context: Context) : ArticleDatabase? {
            if (INSTANCE == null) {
                synchronized(ArticleDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            ArticleDatabase::class.java,
                            DATABASE_NAME).build()
                }
            }
            return INSTANCE
        }
    }
}