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


package com.wanderfar.expander.DynamicPhrases

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanderfar.expander.DynamicPhrases.DynamicPhraseGenerator
import com.wanderfar.expander.DynamicPhrases.DynamicValueAdapter
import com.wanderfar.expander.R
import com.wanderfar.expander.Utilities.RecyclerItemClickListener


class DynamicValueDialogFragment : DialogFragment(){

    interface DynamicValueDialogListener {
        fun onFinishDialog(dynamicValue : String)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.dialog_fragment_dynamic_value, null)

        val recyclerView = v.findViewById(R.id.dynamicValueRecyclerView) as RecyclerView
        val adapter = DynamicValueAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.setItems(DynamicPhraseGenerator.dynamicPhraseOptions.toMutableList())
        adapter.notifyDataSetChanged()

        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(activity, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val activity = activity as DynamicValueDialogListener
                activity.onFinishDialog(adapter.getDynamicValue(position))
                dismiss()
            }
            override fun onItemLongClick(view: View, position: Int) {

            }
        }))

        val cancelButton = v.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dismiss()
        }

        return v
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity)
        dialog.setTitle("Select a Dynamic Value")
        return dialog
    }
}