package pl.gawor.android.tayckner.common.util

import android.util.Log

object Util {

    private  val TAG = "TAYCKNER"

    fun convertMonth(number: String): String {
        return when(number) {
            "01" -> "JAN"
            "02" -> "FEB"
            "03" -> "MAR"
            "04" -> "APR"
            "05" -> "MAY"
            "06" -> "JUN"
            "07" -> "JUL"
            "08" -> "AUG"
            "09" -> "SEP"
            "10" -> "OCT"
            "11" -> "NOV"
            "12" -> "DEC"
            else -> "XXX"
        }
    }

    fun convertMonth(number: Int): String {
        Log.i(TAG, "Util.convertMonth(number = $number)")
        return when(number) {
            0 -> "JAN"
            1 -> "FEB"
            2 -> "MAR"
            3 -> "APR"
            4 -> "MAY"
            5 -> "JUN"
            6 -> "JUL"
            7 -> "AUG"
            8 -> "SEP"
            9 -> "OCT"
            10 -> "NOV"
            11 -> "DEC"
            else -> "XXX"
        }
    }
}