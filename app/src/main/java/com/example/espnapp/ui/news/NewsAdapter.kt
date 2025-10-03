package com.example.espnapp.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.espnapp.databinding.ItemNewsBinding
import com.example.espnapp.model.news.Article
import com.squareup.picasso.Picasso

class NewsAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.Holder>() {

    private val items = mutableListOf<Article>()

    fun submitList(data: List<Article>) {
        items.clear(); items.addAll(data); notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: Holder, pos: Int) = h.bind(items[pos])
    override fun getItemCount() = items.size

    inner class Holder(private val b: ItemNewsBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(a: Article) {
            b.txtTitle.text = a.headline ?: ""
            b.txtDesc.text = a.description ?: ""
            val img = a.images?.firstOrNull()?.url
            if (!img.isNullOrBlank()) Picasso.get().load(img).into(b.img)
            val url = a.links?.web?.href
            b.root.setOnClickListener { if (!url.isNullOrBlank()) onClick(url) }
        }
    }
}
