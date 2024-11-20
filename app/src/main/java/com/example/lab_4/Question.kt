package com.example.lab_4

import androidx.annotation.StringRes

data class Question(@StringRes val textResId:
                    Int, val answer: Boolean)
