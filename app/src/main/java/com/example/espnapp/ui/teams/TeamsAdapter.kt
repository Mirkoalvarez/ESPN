package com.example.espnapp.ui.teams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.espnapp.databinding.ItemTeamBinding
import com.example.espnapp.model.teams.Team
import com.squareup.picasso.Picasso

class TeamsAdapter(private val onClick: (Team) -> Unit) :
    RecyclerView.Adapter<TeamsAdapter.Holder>() {

    private val items = mutableListOf<Team>()
    fun submit(list: List<Team>) { items.clear(); items.addAll(list); notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: Holder, pos: Int) = h.bind(items[pos])
    override fun getItemCount() = items.size

    inner class Holder(private val b: ItemTeamBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(t: Team) {
            b.txtName.text = t.displayName ?: ""
            if (!t.logo.isNullOrBlank()) Picasso.get().load(t.logo).into(b.imgLogo)
            b.root.setOnClickListener { onClick(t) }
        }
    }
}
