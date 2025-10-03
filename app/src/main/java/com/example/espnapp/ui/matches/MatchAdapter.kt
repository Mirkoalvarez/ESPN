package com.example.espnapp.ui.matches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.espnapp.databinding.ItemMatchBinding
import com.example.espnapp.model.scoreboard.Event
import com.squareup.picasso.Picasso

class MatchAdapter : RecyclerView.Adapter<MatchAdapter.Holder>() {
    private val items = mutableListOf<Event>()
    fun submit(list: List<Event>) { items.clear(); items.addAll(list); notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: Holder, pos: Int) = h.bind(items[pos])
    override fun getItemCount() = items.size

    inner class Holder(private val b: ItemMatchBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(e: Event) {
            val comp = e.competitions?.firstOrNull()
            val home = comp?.competitors?.firstOrNull { it.homeAway == "home" }
            val away = comp?.competitors?.firstOrNull { it.homeAway == "away" }

            b.txtTitle.text = e.name ?: ""
            b.txtStatus.text = e.status?.type?.description ?: ""
            b.txtVenue.text = comp?.venue?.fullName ?: ""

            val logoH = home?.team?.logo; if (!logoH.isNullOrBlank()) Picasso.get().load(logoH).into(b.imgHome)
            val logoA = away?.team?.logo; if (!logoA.isNullOrBlank()) Picasso.get().load(logoA).into(b.imgAway)

            b.txtHome.text = "${home?.team?.displayName} ${home?.score ?: ""}"
            b.txtAway.text = "${away?.team?.displayName} ${away?.score ?: ""}"
        }
    }
}
