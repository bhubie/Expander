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
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentTabHost
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanderfar.expander.R
import com.wanderfar.expander.Utilities.RecyclerItemClickListener


class DynamicValueDialogFragment : android.support.v4.app.DialogFragment() {



    interface DynamicValueDialogListener {
        fun onFinishDialog(dynamicValue : String)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.dialog_fragment_dynamic_value, null)


        //Setup the tab hose
        val mTabHost = v?.findViewById(R.id.tabs) as FragmentTabHost
        mTabHost.setup(activity, childFragmentManager)

        mTabHost.addTab(mTabHost.newTabSpec("dynamicValueOptions").setIndicator("Dynamic Value Options"), Fragment::class.java, null)
        mTabHost.addTab(mTabHost.newTabSpec("dynamicValueExamples").setIndicator("Dynamic Value Examples"), Fragment::class.java, null)

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        val viewPager = v.findViewById(R.id.viewpager) as ViewPager
        viewPager.adapter =
                DynamicValueDialogPagerAdapter(childFragmentManager, activity)

        mTabHost.setOnTabChangedListener {
            val i = mTabHost.currentTab
            viewPager.currentItem = i
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i2: Int) {
            }

            override fun onPageSelected(i: Int) {
                mTabHost.currentTab = i
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

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

class DynamicValueDialogPagerAdapter : FragmentPagerAdapter {
    val TAB_COUNT = 2
    lateinit private var context: Context

    constructor(fm: FragmentManager, context: Context) :  super(fm) {
        this.context = context
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 ->  return DynamicValueFragment.newInstance()
            1 ->  return DynamicValueExampleFragment.newInstance()
            else -> {
                return DynamicValueFragment.newInstance()
            }
        }

    }

    override fun getCount(): Int {
        return TAB_COUNT
    }
}


class DynamicValueFragment : Fragment() {



   companion object{
        fun newInstance(): DynamicValueFragment {
            val fragment = DynamicValueFragment()

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.fragment_dynamic_value_options, container, false)

        val recyclerView = view?.findViewById(R.id.dynamicValueRecyclerView) as RecyclerView
        val adapter = DynamicValueAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.setItems(DynamicPhraseGenerator.dynamicPhraseOptions.toMutableList())
        adapter.notifyDataSetChanged()

        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(activity, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val activity = activity as DynamicValueDialogFragment.DynamicValueDialogListener
                activity.onFinishDialog(adapter.getDynamicValue(position))

                //dialogDismissListener.onDismissDialog()
            }
            override fun onItemLongClick(view: View, position: Int) {

            }
        }))


        return view!!
    }
}

class DynamicValueExampleFragment: Fragment(){
    companion object{
        fun newInstance(): DynamicValueExampleFragment {
            val fragment = DynamicValueExampleFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater?.inflate(R.layout.fragment_dynamic_value_examples, container, false)

        val recyclerView = view?.findViewById(R.id.dynamicValueExampleRecyclerView) as RecyclerView
        val adapter = DynamicValueExampleAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.setItems(DynamicPhraseGenerator.dynamicPhraseOptions.toMutableList())
        adapter.notifyDataSetChanged()

        return view!!
    }
}

