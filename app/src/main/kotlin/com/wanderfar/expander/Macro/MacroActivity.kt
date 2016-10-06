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


package com.wanderfar.expander.Macro

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.wanderfar.expander.MainActivity.MainActivity
import com.wanderfar.expander.Models.Macro
import com.wanderfar.expander.Models.MacroConstants
import com.wanderfar.expander.R


class MacroActivity : AppCompatActivity(), MacroActivityView {

    lateinit var macroName : EditText
    lateinit var macroPhrase : EditText
    lateinit var macroDescription : EditText
    lateinit var isCaseSensitive : SwitchCompat
    lateinit var expandWhenContainer : LinearLayout
    lateinit var expandWhenSetting : TextView
    lateinit var addDynamicValueButton : Button
    var expandWhenValue : Int = MacroConstants.ON_A_SPACE_OR_PERIOD
    var mMacroToOpen: String? = null
    var isNewMacro: Boolean = false

    //Create the presenter
    private val mPresenter : MacroActivityPresenter<MacroActivityView> by lazy {
        MacroActivityPresenterImpl(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macro)

        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        macroName = findViewById(R.id.input_name) as EditText
        macroPhrase = findViewById(R.id.input_phrase) as EditText
        macroDescription = findViewById(R.id.input_description) as EditText
        isCaseSensitive = findViewById(R.id.case_sensitive_switch) as SwitchCompat


        mMacroToOpen = intent.getStringExtra(resources.getString(R.string.string_extra_macro_name))

        if (mMacroToOpen.isNullOrEmpty()){
            isNewMacro = true

        }


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        initExpandWhenSettings()

        initAddDynamicValueButton()

        mPresenter.onCreate(mMacroToOpen.toString())
    }


    override fun onResume(){
        super.onResume()

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

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        //code here

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_macro, menu)



        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_trash) {
            if(isNewMacro.not()){
                mPresenter.deleteMacro(mMacroToOpen.toString())
            }
            return true
        }

        if (id == R.id.action_save){
            mPresenter.saveMacro(macroName.text.toString(), macroPhrase.text.toString(),
                    macroDescription.text.toString(),
                    expandWhenValue ,isCaseSensitive.isChecked, isNewMacro)
        }


        return super.onOptionsItemSelected(item)
    }




    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun setData(macro: Macro) {

        val items = resources.getStringArray(R.array.expand_when_labels)
        macroName.setText(macro.name)
        macroPhrase.setText(macro.phrase)
        expandWhenSetting.text = items[macro.expandWhenSetting].toString()

        if (macro.description.isNullOrEmpty().not()){
            macroDescription.setText(macro.description)
        }
    }

    override fun loadData() {

    }


    override fun showDuplicateMacroError() {
        macroName.error = resources.getString(R.string.macro_activity_duplicate_name_error_message)

    }

    override fun showMacroNoNameError() {
        macroName.error = resources.getString(R.string.macro_activity_no_name_error_message)
    }

    override fun showMacroNoPhraseError() {
        macroPhrase.error = resources.getString(R.string.macro_activity_no_phrase_error_message)
    }

    override fun showSavedMacro() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initExpandWhenSettings() {
        expandWhenContainer = findViewById(R.id.caseExpandWhenContainer) as LinearLayout
        expandWhenSetting = findViewById(R.id.expandWhenSummary) as TextView
        expandWhenSetting.text = MacroConstants.ON_A_SPACE_OR_PERIOD.toString()

        val items = resources.getStringArray(R.array.expand_when_labels)

        expandWhenContainer.setOnClickListener ({
            val builder = AlertDialog.Builder(this)
            builder.setTitle("When Should the Shortcut Expand?")

            builder.setItems(items) { dialog, item ->
                expandWhenSetting.text = items[item].toString()
                expandWhenValue = item
            }

            val alert = builder.create()
            alert.show()
        })
    }

    private fun initAddDynamicValueButton() {
        addDynamicValueButton = findViewById(R.id.dynamic_value_button) as Button
        addDynamicValueButton.setOnClickListener {
            println("Button was clicked!")

            val fm = fragmentManager
            val dialogFragment = AddDynamicValueDialogFragment()
            dialogFragment.show(fm, null)
        }
    }

}
