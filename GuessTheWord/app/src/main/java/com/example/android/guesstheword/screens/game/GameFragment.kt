/*
 * Copyright (C) 2019 Google Inc.
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

package com.example.android.guesstheword.screens.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import timber.log.Timber

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Timber.i("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.i("onCreateView")
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
        Timber.i("Called ViewModelProviders.of")
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        // Set the viewmodel for databinding
        binding.gameViewModel = viewModel
        viewModel.score.observe(this, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })
        viewModel.word.observe(this, Observer { newWord ->
            binding.wordText.text = newWord
        })
        viewModel.eventGameFinish.observe(this, Observer { hasFinished ->
            if (hasFinished) gameFinished()
        })
//        binding.correctButton.setOnClickListener { onCorrect() }
//        binding.skipButton.setOnClickListener { onSkip() }
        binding.endGameButton.setOnClickListener { onEndGame() }
//        updateScoreText()
//        updateWordText()
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach")
    }

    /** Methods for buttons presses **/

    private fun onSkip() {
//        if (!wordList.isEmpty()) {
//            score--
//        }
//        nextWord()
        viewModel.onSkip()
//        updateScoreText()
//        updateWordText()
    }

    private fun onCorrect() {
//        if (!wordList.isEmpty()) {
//            score++
//        }
//        nextWord()
        viewModel.onCorrect()
//        updateScoreText()
//        updateWordText()
    }

    private fun onEndGame() {
        gameFinished()
    }

    private fun gameFinished() {
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
        val action = GameFragmentDirections.actionGameToScore()
        action.score = viewModel.score.value ?: 0
        NavHostFragment.findNavController(this).navigate(action)
        viewModel.onGameFinishComplete()
    }
    /** Methods for updating the UI **/

//    private fun updateWordText() {
//        binding.wordText.text = viewModel.word.value
//    }

//    private fun updateScoreText() {
//        binding.scoreText.text = viewModel.score.value.toString()
//    }
}
