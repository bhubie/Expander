/*
 * Expander: Text Expansion Application
 * Copyright (C) 2016 Brett Huber
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wanderfar.expander.DynamicValue

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wanderfar.expander.Application.Expander
import com.wanderfar.expander.DynamicPhraseGenerator.DynamicPhrase
import com.wanderfar.expander.DynamicPhraseGenerator.DynamicPhraseGenerator
import com.wanderfar.expander.R
import java.util.*


class DynamicValueExampleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mDataset: MutableList<DynamicPhrase>? = null
    private var position: Int = 0
    private val TYPE_HEADER: Int = 0
    private val TYPE_ITEM: Int = 1

    override fun getItemViewType(position: Int): Int {
        //if (check condition here with your listData)) // if it is headerView return header type
        if (mDataset?.get(position)?.name == "Date/Time" || mDataset?.get(position)?.name == "Misc"){
            return TYPE_HEADER
        } else {
            return TYPE_ITEM
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var name: TextView
        var example: TextView

        init {
            name = v.findViewById(R.id.dynamic_value_name) as TextView
            example = v.findViewById(R.id.dynamic_value_example) as TextView
        }

    }

    inner class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var headerTitle: TextView

        init {
            headerTitle = v.findViewById(R.id.dynamic_value_header) as TextView

        }

    }


    // Provide a suitable constructor (depends on the kind of dataset)
    fun DynamicValueExampleAdapter(context: Context)  {

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {


        // create a new view
        if (viewType == TYPE_ITEM){
            // create a new view
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dynamic_value_example, parent, false)
            val vh = ViewHolder(v)
            return vh
        }
        else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dynamic_header, parent, false)
            return  HeaderViewHolder(v)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        when (holder.itemViewType){
            TYPE_ITEM ->  {
                val vh = holder as DynamicValueExampleAdapter.ViewHolder
                vh.name.text = mDataset!![position].name + "\t -> \t"
                vh.example.text = DynamicPhraseGenerator
                        .setDynamicPhraseValue(Expander.context, mDataset!![position].phrase, Locale.getDefault())
            }
            TYPE_HEADER -> {
                val vh = holder as DynamicValueExampleAdapter.HeaderViewHolder
                vh.headerTitle.text = mDataset!![position].name
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return if (mDataset == null) 0 else mDataset!!.size
    }

    fun setItems(items: MutableList<DynamicPhrase>) {
        this.mDataset = items
        this.mDataset?.add(0, DynamicPhrase("Date/Time", "0")) //Add Header for date time
        this.mDataset?.add(11, DynamicPhrase("Misc", "0")) //Add Header for Misc items
    }

}