package com.chirathi.funmaths

import android.app.Activity
import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.util.Random


class SubtractionActivity : AppCompatActivity(), View.OnClickListener {

    // Views
    private lateinit var layoutcontent: LinearLayout
    private lateinit var scorelayout: LinearLayout
    private lateinit var questionNumber: Button
    private lateinit var tvQuiz: TextView
    private lateinit var tvScore: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button

    // Variables
    private lateinit var userResult: String
    private var answerCorrect = false
    private var countDownTimer: CountDownTimer? = null
    private var score = 0
    private var progress = 0
    private var progressTime = 5000
    private var sound = true

    // Animation
    private lateinit var myAnim: Animation
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var viewModel: MathsViewModel
    private var gameClosed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subtraction)
        viewModel = ViewModelProvider(this).get(MathsViewModel::class.java)// Initialize ViewModel
        mediaPlayer = MediaPlayer.create(this, R.raw.click_sound)
        myAnim = AnimationUtils.loadAnimation(this@SubtractionActivity, R.anim.dialog_anim_up)
        val interpolator = NewBounceInterpolator(0.2, 20.0)
        myAnim.setInterpolator(interpolator)

        // Initialize Views
        progressBar = findViewById(R.id.progressBar)
        progressBar.setMax(5000)
        layoutcontent = findViewById(R.id.layoutcontent)
        scorelayout = findViewById(R.id.scorelayout)
        questionNumber = findViewById(R.id.questionNumber)
        tvScore = findViewById(R.id.tvScore)
        tvQuiz = findViewById(R.id.tvQuiz)

        // Set click listeners for buttons
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)

        // Observe LiveData for game status, timer, quiz, and score
        viewModel.gameStatusLiveData.observe(this, Observer { status ->
            gameClosed = status
        })
        viewModel.timerLiveData.observe(this, Observer { time ->
            progressTime = time!!.toInt()
        })
        viewModel.quizLiveData.observe(this, Observer { quiz ->
            getRandomQuizSub(quiz[0], quiz[1], quiz[2])
        })
        viewModel.scoreLiveData.observe(this, Observer { score ->
            this.score = score
            tvScore.text = "Score : $score"
        })
        viewModel.quizNoLiveData.observe(this, Observer { quizNo ->
            questionNumber.text = "Question : $quizNo"
        })

        // Delay showing the content to ensure smoother animation
        val handler = Handler()
        handler.postDelayed({
            layoutcontent.setVisibility(View.VISIBLE)
            if (!gameClosed) {
                setupTimer()
                viewModel.createRandomSubtractionQuiz()
            } else {
                showGameOverDialog(this@SubtractionActivity, score, userResult)
            }
        }, 500)
    }

    // Function to set up the countdown timer
    private fun setupTimer() {
        countDownTimer = object : CountDownTimer(progressTime.toLong(), 1) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.updateTimer(millisUntilFinished)
                progress = millisUntilFinished.toInt()
                progressBar.progress = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                if (!answerCorrect) {
                    viewModel.setGameStatus(true)
                    showGameOverDialog(this@SubtractionActivity, score, userResult)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release resources when activity is destroyed
        countDownTimer?.cancel()
        mediaPlayer.release()
    }

    // Function to get a random number within a specified range
    private fun getRandomNo(max: Int, min: Int): Int {
        val r = Random()
        return r.nextInt(max + 1 - min) + min
    }

    // Function to validate user input
    private fun validate(result: String?) {
        if (!gameClosed) {
            if (userResult.equals(result, ignoreCase = true)) {
                answerCorrect = true
                viewModel.calculateScore(progress)
                viewModel.calculateQuizNo()
                viewModel.createRandomSubtractionQuiz()
            } else {
                viewModel.setGameStatus(true)
                answerCorrect = false
                showGameOverDialog(this@SubtractionActivity, score, userResult)
            }
            if (sound) {
                mediaPlayer = MediaPlayer.create(this, R.raw.click_sound)
                mediaPlayer.start()
            }
        }
    }

    // Function to generate a random addition quiz
    private fun getRandomQuizSub(x: Int, y: Int, z: Int) {
        answerCorrect = false
        countDownTimer?.cancel()
        userResult = z.toString()
        var p = ""
        var q = ""
        p = if (x < 0) {
            "($x)"
        } else {
            x.toString() + ""
        }
        q = if (y < 0) {
            "($y)"
        } else {
            y.toString() + ""
        }
        val quiz = "$p - $q = ?"
        tvQuiz.setText(quiz)
        val a = getRandomNo(4, 1)
        if (a == 1) {
            btn1.text = z.toString() + ""
            btn2.text = (z + 1).toString() + ""
            btn3.text = (z + 3).toString() + ""
            btn4.text = (z + 7).toString() + ""
        } else if (a == 2) {
            btn1.text = (z + 4).toString() + ""
            btn2.text = z.toString() + ""
            btn3.text = (z + 1).toString() + ""
            btn4.text = (z + 3).toString() + ""
        } else if (a == 3) {
            btn1.text = (z + 2).toString() + ""
            btn2.text = (z + 1).toString() + ""
            btn3.text = z.toString() + ""
            btn4.text = (z + 4).toString() + ""
        } else {
            btn1.text = (z + 2).toString() + ""
            btn2.text = (z + 5).toString() + ""
            btn3.text = (z + 1).toString() + ""
            btn4.text = z.toString() + ""
        }
        countDownTimer?.start()
    }

    override fun onClick(v: View) {
        // Handle button clicks
        if (v == btn1) {
            validate(btn1.text.toString())
            btn1.startAnimation(myAnim)
        } else if (v == btn2) {
            validate(btn2.text.toString())
            btn2.startAnimation(myAnim)
        } else if (v == btn3) {
            validate(btn3.text.toString())
            btn3.startAnimation(myAnim)
        } else if (v == btn4) {
            validate(btn4.text.toString())
            btn4.startAnimation(myAnim)
        }
    }

    // Function to display game over dialog
    private fun showGameOverDialog(activity: Activity, myScore: Int, result: String?) {
        countDownTimer?.cancel()
        viewModel.resetTimer()
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_score)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        val scoreTv = dialog.findViewById<TextView>(R.id.tv_score)
        val bestScore = dialog.findViewById<TextView>(R.id.tv_best_score)

        val answer = dialog.findViewById<TextView>(R.id.tv_answer)
        val home = dialog.findViewById<Button>(R.id.tv_home)
        val replay = dialog.findViewById<Button>(R.id.tv_replay)

        // Set texts for dialog views
        answer.text = "Answer : $result"
        scoreTv.text = "Score : $myScore"

        // Check and update high score
        if (myScore > getHighScore()) {
            setHighScore(myScore)
            bestScore.text = "Best : $myScore"
        } else {
            bestScore.text = "Best : " + getHighScore()
        }
        bestScore.visibility = View.VISIBLE

        // Set click listeners for buttons
        home.setOnClickListener {
            dialog.dismiss()
            activity.finish()
        }
        replay.setOnClickListener {
            viewModel.setGameStatus(false)
            score = 0
            viewModel.resetTimer()
            viewModel.resetScore()
            viewModel.resetQuizNo()
            setupTimer()
            dialog.dismiss()
            viewModel.createRandomSubtractionQuiz()
        }
        dialog.show()
    }

    override fun onBackPressed() {
        // Release resources and finish activity
        super.onBackPressed()
        countDownTimer?.cancel()
        mediaPlayer.release()
        finish()
    }

    // Function to get shared preferences
    private fun getPref(): SharedPreferences? {
        return getSharedPreferences("com.chirathi.funmaths", 0)
    }

    // Function to set high score in shared preferences
    private fun setHighScore(value: Int) {
        getPref()!!.edit().putInt("score", value).commit()
    }

    // Function to get high score from shared preferences
    private fun getHighScore(): Int {
        return getPref()!!.getInt("score", 0)
    }
}

