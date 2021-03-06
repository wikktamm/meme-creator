package com.example.memecreator.adapters

import coil.api.load
import com.example.memecreator.db.models.meme.Meme
import kotlinx.android.synthetic.main.row_meme.view.*

class MemeAdapter : MemeAdapterTemplate() {

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val currentMeme = diffUtil.currentList[position] as Meme
        holder.item.apply {
            ivMeme.load(currentMeme.url)
            setOnClickListener {
                onMyMemeClickListener?.let {
                    it(currentMeme)
                }
            }
        }
    }

}