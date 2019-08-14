package com.example.android.guesstheword.screens.score

import androidx.lifecycle.ViewModel
import timber.log.Timber

/**
 * <pre>
 *     author : wangzhichao
 *     e-mail : wangzhichao@adups.com
 *     time   : 2019/08/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ScoreViewModel(finalScore: Int) : ViewModel() {
    val score = finalScore

    init {
        Timber.i("Final score is $finalScore")
    }
}