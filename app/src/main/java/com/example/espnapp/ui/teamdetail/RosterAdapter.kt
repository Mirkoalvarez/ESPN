package com.example.espnapp.ui.teamdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.espnapp.databinding.ItemPlayerBinding
import com.example.espnapp.model.roster.Athlete
import com.squareup.picasso.Picasso

class RosterAdapter(private val onClick: (Athlete) -> Unit) :
    RecyclerView.Adapter<RosterAdapter.Holder>() {

    private val items = mutableListOf<Athlete>()
    fun submit(list: List<Athlete>) { items.clear(); items.addAll(list); notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: Holder, pos: Int) = h.bind(items[pos])
    override fun getItemCount() = items.size

    inner class Holder(private val b: ItemPlayerBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(a: Athlete) {
            b.txtName.text = a.displayName ?: ""
            b.txtSubtitle.text = a.position?.abbr ?: ""
            val photo = a.headshot?.href
            if (!photo.isNullOrBlank()) Picasso.get().load(photo).into(b.img)
            b.root.setOnClickListener { onClick(a) }
        }
    }
}
