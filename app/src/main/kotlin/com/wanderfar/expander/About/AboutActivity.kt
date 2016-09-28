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


package com.wanderfar.expander.About

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wanderfar.expander.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val aboutPage = AboutPage(this)
                .setDescription(resources.getString(R.string.about_activity_main_description))
                .addGroup(resources.getString(R.string.about_activity_contact_group_label))
                .addEmail(resources.getString(R.string.about_activity_email_label))
                .addGroup(resources.getString(R.string.about_activity_version_group_label))
                .addItem(createVersionElement())
                .addGroup(resources.getString(R.string.about_activity_open_source_group_label))
                .addItem(createLibraryPaperElement())
                .addItem(createLibraryAboutPageElement())
                .create()

        setContentView(aboutPage)
    }

    private fun createVersionElement() : Element {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionName
        val versionElement = Element()
        versionElement.title = version

        return versionElement
    }

    private fun createLibraryPaperElement() : Element {
        val libraryElementPaper = Element()
        libraryElementPaper.title = resources.getString(R.string.about_activity_open_source_paper_title)
        val openPaperIntent = Intent(Intent.ACTION_VIEW)
        openPaperIntent.data = Uri.parse(resources.getString(R.string.about_activity_open_source_paper_link))
        libraryElementPaper.intent = openPaperIntent

        return libraryElementPaper
    }

    private fun createLibraryAboutPageElement() : Element {

        val libraryElementAboutPage = Element()
        libraryElementAboutPage.title = resources.getString(R.string.about_activity_open_source_about_page_title)
        val openAboutPageIntent = Intent(Intent.ACTION_VIEW)
        openAboutPageIntent.data = Uri.parse(resources.getString(R.string.about_activity_open_source_about_page_link))
        libraryElementAboutPage.intent = openAboutPageIntent

        return libraryElementAboutPage
    }
}
