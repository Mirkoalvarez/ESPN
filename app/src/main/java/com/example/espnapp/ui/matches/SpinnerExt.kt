package com.example.espnapp.ui.matches

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner

fun Spinner.setOnItemSelectedListenerCompat(onSelected: (position: Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) =
            onSelected(pos)
        override fun onNothingSelected(parent: AdapterView<*>) = Unit
    }
}
