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

import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Contains all the executors used to access background threads and perform actions
 */
class AppExecutors private constructor(
        private var diskIO: Executor,
        private var networkIO: Executor,
        private var mainThread: Executor) {

    companion object {
        fun getInstance(): AppExecutors {
            return AppExecutors(Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(3),
                    MainThreadExecutor())
        }
    }

    fun diskIO(): Executor = this.diskIO
    fun networkIO(): Executor = this.networkIO
    fun mainThread(): Executor = this.mainThread

    private class MainThreadExecutor: Executor {

        private var mainThreadHandler: android.os.Handler = android.os.Handler(Looper.getMainLooper())

        override fun execute(command: Runnable?) {
            mainThreadHandler.post(command)
        }

    }
}