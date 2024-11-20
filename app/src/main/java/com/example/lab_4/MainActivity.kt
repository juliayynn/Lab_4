package com.example.lab_4


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider


private const val TAG="MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT=0

class MainActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    var id_Quistion=1
    var score=0
    var curCheat=0
    private val quizViewModel:QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        val provider: ViewModelProvider = ViewModelProvider(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel:$quizViewModel")


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        trueButton=findViewById(R.id.true_button)
        falseButton=findViewById(R.id.false_button)
        nextButton=findViewById(R.id.next_button)
        cheatButton=findViewById(R.id.cheat_button)
        questionTextView=findViewById(R.id.question_text_view)

        trueButton.setOnClickListener()
        {
            checkAnswer(true)
        }
        falseButton.setOnClickListener()
        {
            checkAnswer(false)
        }

        nextButton.setOnClickListener()
        {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        cheatButton.setOnClickListener()
        {
            val answerIsTrue=quizViewModel.currentQuestionAnswer
            val intent=CheatActivity.newIntent(this@MainActivity,answerIsTrue)

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            {
                val options = ActivityOptions.makeClipRevealAnimation(cheatButton, 0, 0, cheatButton.width, cheatButton.height)
                startActivityForResult(intent,REQUEST_CODE_CHEAT,options.toBundle())
            }else
            {
                startActivityForResult(intent,REQUEST_CODE_CHEAT)
            }

        }
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK)
        {
            return
        }
        if( requestCode== REQUEST_CODE_CHEAT)
        {
            quizViewModel.isCheater=data?.getBooleanExtra(EXTRA_ANSWER_SHOWN,false)?:false
        }
    }

    override fun onStart()
    {
        super.onStart()
        Log.d(TAG,"onStart() called")
    }

    override fun onResume()
    {
        super.onResume()
        Log.d(TAG,"onResume() called")
    }

    override fun onPause()
    {
        super.onPause()
        Log.d(TAG,"onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle)
    {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG,"onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX,quizViewModel.currentIndex)
    }
    override fun onStop()
    {
        super.onStop()
        Log.d(TAG,"onStop() called")
    }

    override fun onDestroy()
    {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }

    private fun updateQuestion(){

        trueButton.visibility= View.VISIBLE
        falseButton.visibility= View.VISIBLE
        id_Quistion++
        if(id_Quistion==6)
            nextButton.visibility=View.INVISIBLE

        if(curCheat==3)
        {
            cheatButton.visibility=View.INVISIBLE
        }else
        {
            cheatButton.visibility=View.VISIBLE
        }
        val questionTextResId=quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer:Boolean)
    {
        trueButton.visibility= View.INVISIBLE
        falseButton.visibility= View.INVISIBLE
        cheatButton.visibility=View.INVISIBLE

        val correctAnswer:Boolean=quizViewModel.currentQuestionAnswer

        val messageResId=when{
            quizViewModel.isCheater-> {
                curCheat++
                quizViewModel.isCheater=false
                R.string.judgment_toast
            }
            userAnswer==correctAnswer->R.string.correct_toast
            else->R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        if (userAnswer==correctAnswer)
        {
            score++
        }
        if(curCheat==3)
        {
            cheatButton.visibility=View.INVISIBLE
        }
        if(id_Quistion==6)
        {
            Toast.makeText(this,"Итоговый счет: "+score.toString(),Toast.LENGTH_SHORT).show()
        }
    }
}