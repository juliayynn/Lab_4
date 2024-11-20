package com.example.lab_4

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

const val EXTRA_ANSWER_SHOWN="com.bignerdranch.android.lab_4.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE="com.bignerdranch.android.lab_4.answer_is_true"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView:TextView
    private lateinit var showAnswerButton:Button
    private lateinit var versionSDK:TextView
    private var answerIsTrue=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cheat)

        answerIsTrue=intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false)
        answerTextView=findViewById(R.id.answer_text_view)
        showAnswerButton=findViewById(R.id.show_answer_button)
        versionSDK=findViewById(R.id.VersionSDK)
        showAnswerButton.setOnClickListener()
        {
            val answerText=when{
                answerIsTrue->R.string.true_button
                else-> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }
        versionSDK.setText("API Level "+Build.VERSION.SDK_INT.toString())
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setAnswerShownResult(isAnswerShown:Boolean)
    {
        val data=Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown)
        }
        setResult(Activity.RESULT_OK,data)
    }
    companion object{
        fun newIntent(packageContext: Context,answerIsTrue:Boolean): Intent {
            return Intent(packageContext,CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue)
            }
        }
    }
}