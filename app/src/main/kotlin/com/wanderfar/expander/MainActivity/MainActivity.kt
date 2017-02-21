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

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.widget.TextView
import com.wanderfar.expander.Macro.MacroActivity
import com.wanderfar.expander.About.AboutActivity
import com.wanderfar.expander.Models.Macro
import com.wanderfar.expander.Models.MacroConstants
import com.wanderfar.expander.R
import com.wanderfar.expander.Settings.SettingsActivity
import com.wanderfar.expander.Utilities.RecyclerItemClickListener
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : AppCompatActivity(), MainActivityView {


    lateinit var mAdapter : MacroListAdapter

    //Create the presenter
    private val mPresenter : MainActivityPresenter<MainActivityView> by lazy {
        MainActivityPresenterImpl(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //Expander.graph.inject(this)

        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton?
        fab!!.setOnClickListener({
            val intent = Intent(this, MacroActivity::class.java)
            startActivity(intent)
        })

        //checkIfAccessibilityPermissionIsEnabled()

        initRecyclerView()

        initPullToRefresh()

        mPresenter.onCreate()
    }


    override fun onResume(){
        super.onResume()

        checkIfAccessibilityPermissionIsEnabled()
        mPresenter.onResume()
    }

    override fun onPause(){
        super.onPause()
    }

    override fun onStop(){
        super.onStop()
    }

    override fun onDestroy(){
        mPresenter.onDestroy()
        super.onDestroy()
    }



    override fun showProgress() {
        pullToRefresh.isRefreshing = true
    }

    override fun hideProgress() {
        pullToRefresh.isRefreshing = false
    }

    override fun setData(macros: MutableList<Macro>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        mAdapter.setData(macros, prefs.getInt("SortByMethod", MacroConstants.SORT_BY_NAME))
        mAdapter.notifyDataSetChanged()

    }

    override fun loadData(macros: MutableList<Macro>) {

    }

    override fun showNoMacroFoundMessage() {
        noMacroFound.visibility = View.VISIBLE
    }

    override fun setMacroListSortPreference(sortPreference: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        editor.putInt("SortByMethod", sortPreference)
        editor.apply()
    }

    override fun sortMacroListAdapter(sortBy: Int) {
        mAdapter.sortList(sortBy)
    }

    override fun refreshMenu() {
        invalidateOptionsMenu()
    }

    private fun checkIfAccessibilityPermissionIsEnabled() {

        if (isAccessibilityEnabled("com.wanderfar.expander/.MacroAccessibilityService.MacroAccessibilityService")){
            println("Permission Enabled!")
            println("Package name is: " + applicationContext.packageName)
        }
        else {
            println("Permission Not Enabled!")
            println("Package name is: " + applicationContext.packageName)

            val accessibilitySnackbarClickListener = View.OnClickListener {
                intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivityForResult(intent, 0)
            }

            val snackbar = Snackbar.make(findViewById(R.id.rootview) as View,
                    "Please enable Accessibility Settings permission for " + resources.getString(R.string.app_name) + ". This is a required permission for the app to work.",
                    Snackbar.LENGTH_INDEFINITE)
                        .setAction(resources.getString(R.string.main_activity_accessibility_settings_snackbar_link_label)
                                , accessibilitySnackbarClickListener)
                        .setActionTextColor(resources.getColor(R.color.colorAccent))

            val snackbarView = snackbar.view
            val tv = snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
            tv.maxLines = 3
            snackbar.show()

        }

    }

    private fun isAccessibilityEnabled(id: String) : Boolean {
        val mAccessibilityManager =  this.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager


        val runningServices: List<AccessibilityServiceInfo>  = mAccessibilityManager
                //.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK)
                .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK)

        for (service: AccessibilityServiceInfo in runningServices) {

            println("Services are:" + service.packageNames)
            println("Service ID's: " + service.id)
            if (id == service.id) {
                return true
            }
        }

        return false
    }


    private fun initRecyclerView() {

        mAdapter = MacroListAdapter(this)
        noteListRecyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        noteListRecyclerView.adapter = mAdapter

        noteListRecyclerView.addOnItemTouchListener(RecyclerItemClickListener(this, noteListRecyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val bundle = Bundle()
                bundle.putString(resources.getString(R.string.string_extra_macro_name), mAdapter.getMacroName(position))
                val intent = Intent(applicationContext, MacroActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)

            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        }))
    }

    private fun initPullToRefresh() {
        pullToRefresh.setColorSchemeResources(R.color.colorAccent)
        pullToRefresh.setOnRefreshListener { mPresenter.onCreate() }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean{

        val sortByShortcutName = menu.findItem(R.id.sortByName)
        val sortByShortcutUsageCount = menu.findItem(R.id.sortByUsageCount)
        val sortByLastUsed = menu.findItem(R.id.sortByLastUsed)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val sortBySetting = prefs.getInt("SortByMethod", MacroConstants.SORT_BY_NAME)

        when (sortBySetting) {
            MacroConstants.SORT_BY_NAME  -> sortByShortcutName.isChecked = true
            MacroConstants.SORT_BY_USAGE_COUNT -> sortByShortcutUsageCount.isChecked = true
            MacroConstants.SORT_BY_LAST_USED -> sortByLastUsed.isChecked = true
            else -> sortByShortcutName.isChecked = true
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.action_settings) {
            val intent1 = Intent(this, SettingsActivity::class.java)
            startActivity(intent1)
            return true
        }

        if (id == R.id.action_about){
            val aboutIntent = Intent(this, AboutActivity::class.java)
            startActivity(aboutIntent)
            return true
        }

        if (id == R.id.sortByName){
            mPresenter.setMacroSort(MacroConstants.SORT_BY_NAME)
            return true
        }

        if (id == R.id.sortByUsageCount){
            mPresenter.setMacroSort(MacroConstants.SORT_BY_USAGE_COUNT)
            return true
        }

        if (id == R.id.sortByLastUsed){
            mPresenter.setMacroSort(MacroConstants.SORT_BY_LAST_USED)
            return true
        }

        return super.onOptionsItemSelected(item)
    }



}
