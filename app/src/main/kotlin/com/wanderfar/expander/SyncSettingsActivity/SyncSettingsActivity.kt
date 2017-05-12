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

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

    override fun setSyncSettingsSwitch(boolean: Boolean) {
        isSyncEnabled.isChecked = boolean
    }

    override fun showSyncProviderDialog() {
        val syncProviders = resources.getStringArray(R.array.sync_providers)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(getString(R.string.sync_settings_activity_sync_provider_dialog_title))
                .setItems(syncProviders, { dialogInterface, i ->
                    mPresenter.setupSyncFor(i)
                })
                .create()
                .show()
    }

    override fun showGoogleDriveAuthorizationRequest() {

    }

    override fun showDropboxAuthorizationRequest() {
       
    }

    private fun initSyncOnOffSwitchListener(){
        isSyncEnabled.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                mPresenter.turnOnSync()
            } else {
                mPresenter.turnOffSync()
            }
        }

    }
}
