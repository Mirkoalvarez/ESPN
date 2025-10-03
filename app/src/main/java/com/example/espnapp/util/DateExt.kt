package com.example.espnapp.util

import java.text.SimpleDateFormat
import java.util.*

fun todayYyyyMmDd(): String = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
fun dateToYyyyMmDd(year: Int, month0: Int, day: Int): String =
    String.format(Locale.US, "%04d%02d%02d", year, month0 + 1, day)
