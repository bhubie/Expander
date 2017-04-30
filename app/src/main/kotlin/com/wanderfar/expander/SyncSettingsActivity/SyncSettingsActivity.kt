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

package com.wanderfar.expander.SyncSettingsActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.appcompat.R.styleable.CompoundButton
import com.wanderfar.expander.AppSettings.AppSettingsImpl

import com.wanderfar.expander.R
import kotlinx.android.synthetic.main.activity_sync_settings.*

class SyncSettingsActivity : AppCompatActivity(), SyncSettingsActivityView {


    //Create the presenter
    private val mPresenter: SyncSettingsActivityPresenter<SyncSettingsActivityView> by lazy {
        SyncSettingsActivityPresenterImpl(this, AppSettingsImpl(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync_settings)

        mPresenter.onCreate()
        initSyncOnOffSwitchListener()

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


    override fun disableSyncSettingFields() {
    }

    override fun enableSyncSettingFields() {

    }

    private fun initSyncOnOffSwitchListener(){
        isSyncEnabled.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                mPresenter.turnOffSync()
            } else {
                mPresenter.turnOnSync()
            }
        }

    }
}
