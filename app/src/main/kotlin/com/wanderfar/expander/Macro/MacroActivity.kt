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
import com.wanderfar.expander.DynamicPhrases.DynamicValueDialogFragment
import com.wanderfar.expander.DynamicPhrases.DynamicValueEditText
import com.wanderfar.expander.MainActivity.MainActivity
import com.wanderfar.expander.Models.Macro
import com.wanderfar.expander.Models.MacroConstants
import com.wanderfar.expander.R


class MacroActivity : AppCompatActivity(), MacroActivityView, DynamicValueDialogFragment.DynamicValueDialogListener {


    lateinit var macroName : EditText
    lateinit var macroPhrase : DynamicValueEditText
    lateinit var macroDescription : EditText
    lateinit var isCaseSensitive : SwitchCompat
    lateinit var expandWhenContainer : LinearLayout

    lateinit var expandWhenSetting : TextView
    lateinit var addDynamicValueButton : Button


    var originalName: String = ""
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
        macroPhrase = findViewById(R.id.input_phrase) as DynamicValueEditText
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

        if(id == android.R.id.home){
            onBackPressed()
            return true
        }
        if (id == R.id.action_trash) {
            if(isNewMacro.not()){
                mPresenter.deleteMacro(mMacroToOpen.toString())
            }
            return true
        }

        if (id == R.id.action_save){
            saveMacro()
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
        originalName = macro.name
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

    override fun onFinishDialog(dynamicValue: String) {
        //Before updating text, check if the last character contains a space.
        //If it doesn't and the phrase isn't empty append a space with the dynamic value

        if (macroPhrase.text.length == 0 || macroPhrase.text.last().isWhitespace()){
            macroPhrase.append(dynamicValue + " ")
        } else {
            macroPhrase.append(" $dynamicValue ")
        }
    }

    override fun askIfUserWantsToSaveChanges() {
        AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
            .setTitle("Changes Found")
            .setMessage("Would you like to save your changes?")
            .setPositiveButton("Save", { dialog, which -> saveMacro() })
            .setNegativeButton("Discard", { dialog, which -> goBack() })
            .create()
            .show()
    }

    override fun onBackPressed(){

        if (isNewMacro.not()){
            mPresenter.checkIfMacroIsChanged(originalName, macroName.text.toString(), macroPhrase.text.toString(),
                    macroDescription.text.toString(),
                    expandWhenValue, isCaseSensitive.isChecked, isNewMacro)
        } else {
            goBack()
        }

    }

    override fun goBack() {
       super.onBackPressed()
    }

    override fun saveMacro() {

        mPresenter.saveMacro(originalName, macroName.text.toString(), macroPhrase.text.toString(),
                macroDescription.text.toString(),
                expandWhenValue ,isCaseSensitive.isChecked, isNewMacro)

    }

    private fun initExpandWhenSettings() {
        expandWhenContainer = findViewById(R.id.caseExpandWhenContainer) as LinearLayout
        expandWhenSetting = findViewById(R.id.expandWhenSummary) as TextView


        val items = resources.getStringArray(R.array.expand_when_labels)
        expandWhenSetting.text = items[MacroConstants.ON_A_SPACE_OR_PERIOD]

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
            val dialogFragment = DynamicValueDialogFragment()
            dialogFragment.show(fm, null)
        }
    }

}
