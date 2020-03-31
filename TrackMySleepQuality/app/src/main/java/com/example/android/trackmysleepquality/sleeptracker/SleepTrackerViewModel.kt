/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) { // AndroidViewModel 会接收一个 Application 的 context。
    // 作用是当 viewmodel 不再使用或者销毁时，让我们可以取消由这个 viewmodel 开启的所有 corountines。
    // This way, you don't end up with coroutines that have nowhere to return to.
    private var viewModelJob = Job()
    // The scope determines what thread the coroutine will run on, and the scope
    // also needs to know about the job.
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob) // + 就是 plus 函数

    val nights: LiveData<List<SleepNight>> = database.getAllNights()

    // 这里是做了转换，跟 RxJava 的 map 类似。
    val nightString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }
    // 持有当前的night，设置为 MutableLiveData 是因为不仅要监控数据也要改变数据。
    private var tonight = MutableLiveData<SleepNight?>()

    // 控制 button 是否可用
    val startButtonVisible = Transformations.map(tonight) {
        it == null
    }
    val stopButtonVisible = Transformations.map(tonight) {
        it != null
    }
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    private val _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    // 这里返回可空类型，是因为数据库中可能不存在需要的 SleepNight 数据
    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                // endTimeMilli 和 startTimeMilli 不相等，表示 night 已经被完成了。
                night = null
            }
            night
        }

    }

    // 点击 start 按钮事件的点击事件处理器
    // 这是采用了 listener bindings 的方式
    fun onStartTracking() {
        uiScope.launch {
            // 创建新的 SleepNight，此时 startTimeMill 和 endTImeMill 是相等的。
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
        }
    }

    // 重置触发导航的变量
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
        _showSnackbarEvent.value = true
    }

    override fun onCleared() {
        super.onCleared()
        // 取消所有的 coroutines。
        viewModelJob.cancel()
    }
}

