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

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SleepDatabaseDao {
    // insert new night
    @Insert
    fun insert(night: SleepNight)

    // update an existing night to update an end time and a quality rating.
    @Update
    fun update(night: SleepNight)

    // get a specified night based on key.
    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId = :key") // :key 表示引用函数中的参数
    fun get(key: Long): SleepNight?

    // delete all entries in the database
    @Query("DELETE FROM daily_sleep_quality_table") // 清空数据,并不会把表删掉
    fun clear()

    // get the most recent night
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getTonight(): SleepNight? // 返回的是可空类型

    // get all nights, so you can display it
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>
}
