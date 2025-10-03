package com.example.espnapp.ui.player

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.espnapp.data.remote.EspnApis
import com.example.espnapp.databinding.ActivityPlayerDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.Calendar

class PlayerDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PLAYER_ID = "player_id"
        const val EXTRA_PLAYER_NAME = "player_name"
        const val EXTRA_PLAYER_POS = "player_pos"
        const val EXTRA_PLAYER_PHOTO = "player_photo"
    }

    private lateinit var b: ActivityPlayerDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityPlayerDetailBinding.inflate(layoutInflater)
        setContentView(b.root)

        val id = intent.getStringExtra(EXTRA_PLAYER_ID) ?: return finish()
        b.txtTitle.text = intent.getStringExtra(EXTRA_PLAYER_NAME) ?: ""
        b.txtPos.text = intent.getStringExtra(EXTRA_PLAYER_POS) ?: ""
        intent.getStringExtra(EXTRA_PLAYER_PHOTO)?.let { Picasso.get().load(it).into(b.img) }

        b.viewError.btnRetry.setOnClickListener { loadStats(id) }
        loadStats(id)
    }

    private fun loadStats(athleteId: String) {
        b.progress.visibility = View.VISIBLE
        b.viewError.root.visibility = View.GONE
        b.txtStats.text = ""
        lifecycleScope.launch {
            try {
                val season = Calendar.getInstance().get(Calendar.YEAR)
                val stats = EspnApis.core.getPlayerStats(athleteId, season)
                val sb = StringBuilder()
                stats.splits?.categories.orEmpty().forEach { cat ->
                    val title = cat.name ?: "Stats"
                    sb.appendLine("• " + title)
                    cat.stats.orEmpty().forEach { st ->
                        sb.appendLine("   - " + (st.displayName ?: st.name) + ": " + (st.value ?: "-"))
                    }
                }
                b.txtStats.text = if (sb.isNotEmpty()) sb.toString() else "No stats available."
            } catch (e: Exception) {
                b.viewError.root.visibility = View.VISIBLE
                b.viewError.txtError.text = "Failed to load player stats."
            } finally {
                b.progress.visibility = View.GONE
            }
        }
    }
}
