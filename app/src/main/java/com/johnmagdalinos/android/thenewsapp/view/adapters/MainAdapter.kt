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

package com.johnmagdalinos.android.thenewsapp.view.adapters

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnmagdalinos.android.thenewsapp.R
import com.johnmagdalinos.android.thenewsapp.model.Article
import com.johnmagdalinos.android.thenewsapp.view.adapters.MainAdapter.ViewHolder
import kotlinx.android.synthetic.main.article_list_item.view.*

/**
 * Adapter for the RecyclerView used in the MainFragment. Uses DiffUtil to compare different
 * versions of a list of Articles
 */
class MainAdapter(
        private val context: Context,
        private val listener: (Int) -> Unit) : ListAdapter<Article, ViewHolder>(DIFF_CALLBACK){

    var list: List<Article>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.article_list_item,
                parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindTo(getItem(position), listener)

    /** Used to submit a new list. Starts DiffUtil */
    override fun submitList(list: List<Article>?) {
        this.list = list
        super.submitList(list)
    }

    /** Used to retrieve the item at a specific position */
    fun getItemAtPosition(position: Int) : Article? = if (list!= null) list!![position] else null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(article: Article?, listener: (Int) ->
        Unit) {
            // Get the appropriate data into the TextViews
            itemView.tv_title.text = article?.title
            itemView.tv_author.text = article?.byline
            itemView.tv_date.text = article?.published_date

            // Setup the onClickListener for the entire ViewHolder
            itemView.setOnClickListener {
                // Pass the adapter position to the listener
                listener(adapterPosition)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id            }

        }
    }

}