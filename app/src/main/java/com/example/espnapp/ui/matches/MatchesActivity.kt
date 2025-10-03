package com.example.espnapp.ui.matches

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.espnapp.common.UiState
import com.example.espnapp.databinding.ActivityMatchesBinding
import com.example.espnapp.util.dateToYyyyMmDd
import com.example.espnapp.util.todayYyyyMmDd
import java.util.Calendar
import androidx.recyclerview.widget.LinearLayoutManager

class MatchesActivity : AppCompatActivity() {
    private lateinit var b: ActivityMatchesBinding
    private val vm: MatchesViewModel by viewModels()
    private val adapter = MatchAdapter()

    private var selectedLeague = "eng.1"
    private var selectedDate = todayYyyyMmDd()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMatchesBinding.inflate(layoutInflater)
        setContentView(b.root)


        b.recycler.layoutManager = LinearLayoutManager(this)
        b.recycler.adapter = adapter

        val leagues = listOf(
            "Premier League" to "eng.1",
            "MLS" to "usa.1",
            "Europa League" to "uefa.europa"
        )
        b.spnLeague.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, leagues.map { it.first }
        )

        b.spnLeague.setOnItemSelectedListenerCompat { position ->
            selectedLeague = leagues[position].second
            vm.load(selectedLeague, selectedDate)
        }

        b.btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                selectedDate = dateToYyyyMmDd(y, m, d)
                b.txtDate.text = selectedDate
                vm.load(selectedLeague, selectedDate)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        b.viewError.btnRetry.setOnClickListener { vm.load(selectedLeague, selectedDate) }
        b.txtDate.text = selectedDate
        vm.state.observe(this) { state ->
            when (state) {
                is UiState.Loading -> render(loading = true)
                is UiState.Success -> { adapter.submit(state.data); render(list = state.data.isNotEmpty()) }
                is UiState.Empty -> render(empty = true)
                is UiState.Error -> render(error = state.message)
            }
        }
        vm.load(selectedLeague, selectedDate)
    }

    private fun render(loading: Boolean=false, list:Boolean=false, empty:Boolean=false, error:String?=null) {
        b.progress.visibility = if (loading) View.VISIBLE else View.GONE
        b.recycler.visibility = if (list) View.VISIBLE else View.GONE
        b.viewEmpty.root.visibility = if (empty) View.VISIBLE else View.GONE
        b.viewError.root.visibility = if (error!=null) View.VISIBLE else View.GONE
        b.viewError.txtError.text = error ?: ""
    }
}
