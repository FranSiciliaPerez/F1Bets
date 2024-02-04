package com.example.f1bets

import android.widget.EditText

class Funciones {

    companion object{
        /**
         * @param et: EditText
         * @return
         */
        fun cleanText(et: EditText): String =  et.text.toString().trim()

        /**
         * @param data:String
         * @return
         */
        fun allFilled(vararg data: String): Boolean = data.all { it.isNotEmpty() || it.isNotBlank() }

    }
}