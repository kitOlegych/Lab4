package com.example.lab4

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import kotlin.time.Duration

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton : Button
    private lateinit var falseButton : Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private val questionBank = listOf(
        Question(R.string.question_australia, true,false),
        Question(R.string.question_oceans, true,false),
        Question(R.string.question_mideast, false,false),
        Question(R.string.question_africa, false,false),
        Question(R.string.question_americas, true,false),
        Question(R.string.question_asia, true,false))
    private var currentIndex = 0
    private var corAnswCount = 0
    private var answCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }
        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        prevButton.setOnClickListener {
            currentIndex = (currentIndex - 1).mod(questionBank.size)
            updateQuestion()
        }
        currentIndex = savedInstanceState?.getInt("indexkey", 0) ?: 0
        Log.d("TestInstance","FirstUpdateQuestion:$currentIndex")
        updateQuestion()
    }
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
        if (currentIndex == questionBank.size - 1)
            nextButton.visibility = View.GONE
        else
            nextButton.visibility = View.VISIBLE
        if (currentIndex == 0)
            prevButton.visibility = View.GONE
        else
            prevButton.visibility = View.VISIBLE

        if(questionBank[currentIndex].isAnswered == false) {
            trueButton.visibility = View.VISIBLE
            falseButton.visibility = View.VISIBLE
        }
        else{
            trueButton.visibility = View.GONE
            falseButton.visibility = View.GONE
        }
    }
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId: Int
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            corAnswCount++
        } else {
            messageResId = R.string.incorrect_toast
        }
        questionBank[currentIndex].isAnswered = true
        answCount++

        Toast.makeText(this, messageResId,Toast.LENGTH_SHORT).show()

        trueButton.visibility = View.GONE
        falseButton.visibility = View.GONE

        if(answCount == questionBank.size)
            questionTextView.setText("Количество правильных ответов:$corAnswCount")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("indexkey", currentIndex)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentIndex = savedInstanceState.getInt("indexkey")
        updateQuestion()
        Log.d("TestInstance","Restore Index:$currentIndex")
    }
}
