package com.chirathi.funmaths

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Random

class MathsViewModel : ViewModel() {

    // Variables to hold game state and data
    private var currentScore = 0
    private var currentQuizNo = 1
    private var time = 5000L
    private var randomMax = 10
    private var randomMin = 0
    private var gameClosed = false
    private var x = 0
    private var y = 0
    private var z = 0

    // LiveData objects for observing data changes
    private val _scoreLiveData = MutableLiveData<Int>()
    val scoreLiveData: LiveData<Int> get() = _scoreLiveData
    private val _quizNoLiveData = MutableLiveData<Int>()
    val quizNoLiveData: LiveData<Int> get() = _quizNoLiveData
    private val _quizLiveData = MutableLiveData<List<Int>>()
    val quizLiveData: LiveData<List<Int>> get() = _quizLiveData
    private val _timerLiveData = MutableLiveData<Long>()
    val timerLiveData: LiveData<Long> get() = _timerLiveData
    private val _gameStatusLiveData = MutableLiveData<Boolean>()
    val gameStatusLiveData: LiveData<Boolean> get() = _gameStatusLiveData

    // Function to reset the score to zero
    fun resetScore() {
        currentScore = 0
        _scoreLiveData.postValue(currentScore)
    }

    // Function to calculate and update the score
    fun calculateScore(progress: Int) {
        currentScore += Math.round((progress / 350).toFloat())
        time = 5000L
        _timerLiveData.postValue(time)
        _scoreLiveData.postValue(currentScore)
    }

    // Function to reset the current quiz number
    fun resetQuizNo() {
        currentQuizNo = 1
        _quizNoLiveData.postValue(currentQuizNo)
    }

    // Function to calculate and update the current quiz number
    fun calculateQuizNo() {
        currentQuizNo++
        _quizNoLiveData.postValue(currentQuizNo)
    }

    // Function to generate a random number within a specified range
    private fun getRandomNo(max: Int, min: Int): Int {
        val r = Random()
        return r.nextInt(max + 1 - min) + min
    }

    // Function to create a random addition quiz
    fun createRandomAdditionQuiz() {
        if (time == 5000L) {
            x = getRandomNo(randomMax, randomMin)
            y = getRandomNo(randomMax, randomMin)
        }
        z = x + y
        _quizLiveData.postValue(listOf(x, y, z, 1))
    }

    // Function to create a random subtraction quiz
    fun createRandomSubtractionQuiz() {
        if (time == 5000L) {
            val x1 = getRandomNo(randomMax, randomMin)
            val y1 = getRandomNo(randomMax, randomMin)
            if (x1 < y1) {
                y = x1
                x = y1
            } else {
                y = y1
                x = x1
            }
        }
        val z = x - y
        _quizLiveData.postValue(listOf(x, y, z, 2))
    }

    // Function to create a random Multiplication quiz
    fun createRandomMultiplicationQuiz() {
        if (time == 5000L) {
            x = getRandomNo(randomMax, randomMin)
            y = getRandomNo(randomMax, randomMin)
        }
        z = x * y
        _quizLiveData.postValue(listOf(x, y, z, 3))
    }

    // Function to create a random division quiz
    fun createRandomDivisionQuiz() {
        if (time == 5000L) {
            x = getRandomNo(randomMax, randomMin)
            y = getRandomNo(randomMax, randomMin)
        }
        if (x == 0) x++
        if (y == 0) y++
        z = x * y / y
        _quizLiveData.postValue(listOf(x, y, z, 4))
    }

    // Function to reset the game timer
    fun resetTimer() {
        time = 5000L
        _timerLiveData.postValue(time)
    }

    // Function to update the game timer
    fun updateTimer(currentTime: Long) {
        time = currentTime
        _timerLiveData.postValue(time)
    }

    // Function to set the game status (open or closed)
    fun setGameStatus(status: Boolean) {
        gameClosed = status
        _gameStatusLiveData.postValue(gameClosed)
    }

}