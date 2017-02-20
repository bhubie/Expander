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


package com.wanderfar.expander.MainActivity

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wanderfar.expander.DynamicValue.DynamicValueTextView
import com.wanderfar.expander.Models.Macro
import com.wanderfar.expander.Models.MacroConstants
import com.wanderfar.expander.R
import java.text.SimpleDateFormat


class MacroListAdapter
(context: Context) : RecyclerView.Adapter<MacroListAdapter.ViewHolder>() {

    private var mDataset: MutableList<Macro>? = null
    var position: Int = 0


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var macroName: TextView
        var macroPhrase: TextView
        var usageCount: TextView
        var lastUsed: TextView

        init {
            macroName = v.findViewById(R.id.macroName) as TextView
            macroPhrase = v.findViewById(R.id.macroPhrase) as DynamicValueTextView
            usageCount = v.findViewById(R.id.usageCount) as TextView
            lastUsed = v.findViewById(R.id.lastUsed) as TextView

        }


    }

    fun add(position: Int, macro: Macro) {
        mDataset!!.add(position, macro)
        notifyItemInserted(position)
    }

    
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MacroListAdapter.ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.detail_macro_list, parent, false)
        // set the view's size, margins, paddings and layout parameters
        val vh = ViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.macroName.text = mDataset!![position].name
        holder.macroPhrase.text = mDataset!![position].phrase
        holder.usageCount.text = mDataset!![position].usageCount.toString()

        if (mDataset!![position].lastUsed != null){

            //holder.lastUsed.text = mDataset!![position].lastUsed.toString()
            val format =SimpleDateFormat("MM/dd/yyyy")
            holder.lastUsed.text = format.format(mDataset!![position].lastUsed)
        } else {
            holder.lastUsed.text = "Never Used"
        }

        holder.itemView.isLongClickable = true
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return if (mDataset == null) 0 else mDataset!!.size
    }


    fun getMacroName(position: Int): String {
        return mDataset!![position].name
    }


    fun setData(macro: MutableList<Macro>, sortBy: Int) {

        this.mDataset = macro

        sortList(sortBy)

        notifyDataSetChanged()
    }

    fun sortList(sortBy: Int){
        when (sortBy) {
            MacroConstants.SORT_BY_NAME  -> sortByMacroName()
            MacroConstants.SORT_BY_USAGE_COUNT -> sortByUsageCount()
            else -> sortByMacroName()
        }

        notifyDataSetChanged()
    }

    private fun sortByMacroName(){
        this.mDataset!!.sortBy(Macro::name)
    }

    private fun sortByUsageCount(){
        this.mDataset!!.sortByDescending(Macro::usageCount)
    }

}
