/*
* Copyright (C) 2018 John Magdalinos
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.johnmagdalinos.android.thenewsapp.utils

/**
 * Contains all the constants used throught the application
 */
class Constants {
    companion object {
        // NY Times API
        const val API_KEY_QUERY = "apikey"
        const val BASE_URL = "http://api.nytimes.com/"

        // Keys for saving instance state
        const val MAIN_ACTIVITY_FRAGMENT_STATE: String = "main_activity_fragment"
        const val MAIN_FRAGMENT_RECYCLER_STATE: String = "main_recycler_state"
        const val ALL_SECTIONS: String = "all-sections"

        // Keys for intents
        const val DETAIL_ACTIVITY_URL_STRING: String = "detail_activity_url_string"

        // Keys for preferences
        const val PREFERENCES_TIME: String = "preferences_time"
    }
}