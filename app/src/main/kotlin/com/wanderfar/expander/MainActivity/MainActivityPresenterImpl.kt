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


import com.wanderfar.expander.AppSettings.AppSettings
import com.wanderfar.expander.Models.MacroStore




class MainActivityPresenterImpl(override var view: MainActivityView?, var appSettings: AppSettings?) : MainActivityPresenter<MainActivityView> {

    override fun onCreate() {
        //TODO Load on background thread
        if (appSettings!!.isApplicationFirstStart()){
            appSettings?.setApplicationFirstStart(false)
            view?.launchApplicationIntroductionActivity()
        } else {
            view?.showProgress()
            val macroList = MacroStore.getMacros()
            if (macroList.isNotEmpty()){
                view?.setData(macroList, appSettings?.getMacroListSortByMethod())
            } else {
                view?.showNoMacroFoundMessage()
            }
            view?.hideProgress()
        }
    }

    override fun onResume() {
        if (appSettings?.isAccessibilityServiceEnabled()!!.not()){
            view?.showAccessibilityServiceNotEnabledMessage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appSettings = null

    }

    override fun setMacroSort(sortMethod: Int) {
        appSettings?.setMacroListSortByPreference(sortMethod)
        view?.sortMacroListAdapter(sortMethod)
        view?.refreshMenu()
    }
}