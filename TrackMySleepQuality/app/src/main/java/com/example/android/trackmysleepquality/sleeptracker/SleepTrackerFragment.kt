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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)
        // todo requireNotNull 里面用到了 contract 契约，还不懂
        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        val sleepTrackerViewModel = ViewModelProviders.of(this, viewModelFactory).get(SleepTrackerViewModel::class.java)
        // 设置 binding 的 LifecycleOwner, 设置当前的 Fragment 作为 binding 的 lifecycleOwner。
        binding.lifecycleOwner = this
        binding.sleepTrackerViewModel = sleepTrackerViewModel
        sleepTrackerViewModel.navigateToSleepQuality.observe(this, Observer { night ->
            night?.let {
                this.findNavController().navigate(
                        SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(it.nightId)
                )
                sleepTrackerViewModel.doneNavigating()
            }
        })
        sleepTrackerViewModel.showSnackbarEvent.observe(this, Observer {
            if (it == true) {
                Snackbar.make(activity!!.findViewById(android.R.id.content),
                        R.string.cleared_message, Snackbar.LENGTH_SHORT).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })
        val adapter = SleepNightAdapter(SleepNightListener { nightId ->
            sleepTrackerViewModel.onSleepNightClicked(nightId)
        })
        binding.sleepList.adapter = adapter
        sleepTrackerViewModel.nights.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        sleepTrackerViewModel.navigationToSleepDetail.observe(this, Observer { nightId ->
            nightId?.let {
                findNavController().navigate(
                        SleepTrackerFragmentDirections
                                .actionSleepTrackerFragmentToSleepDetailFragment(nightId))
                sleepTrackerViewModel.onSleepNightNavigated()

            }
        })
        val manager = GridLayoutManager(activity, 3)
        binding.sleepList.layoutManager = manager
        return binding.root
    }
}
