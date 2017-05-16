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

package com.wanderfar.expander.SyncManager

import com.dropbox.core.DbxHost
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.http.OkHttp3Requestor
import com.dropbox.core.v2.DbxClientV2

class DropboxClientFactory {

    companion object {

        var sDbxClient: DbxClientV2? = null

        fun init(accessToken: String){
            if (sDbxClient == null) {
                val requestConfig = DbxRequestConfig.newBuilder("com.wanderfar.expander")
                        .withHttpRequestor(OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                        .build()

                sDbxClient = DbxClientV2(requestConfig, accessToken, DbxHost.DEFAULT)
            }
        }

        fun getClient(): DbxClientV2 {
            if (sDbxClient == null) {
                throw IllegalStateException("Client not initialized.")
            }
            return sDbxClient as DbxClientV2
        }

    }
}