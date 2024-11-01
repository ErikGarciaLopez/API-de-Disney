package com.example.apirestdisney.view.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apirestdisney.data.remote.model.Character
import com.example.apirestdisney.data.remote.model.Data
import com.example.apirestdisney.databinding.CharacterElementBinding
import com.example.apirestdisney.databinding.FragmentCharacterListBinding
import com.example.apirestdisney.view.CharacterListFragment

class CharacterViewHolder(
    private val binding: CharacterElementBinding
) :RecyclerView.ViewHolder(binding.root) {

    fun bind(character: Data) {
        binding.tvTitle.text = character.name

        Glide.with(binding.root.context)
            .load(character.imageUrl)
            //TODO: poner .error(R.drawable.background_character) 1:34:30 y tambien placeholder
            .into(binding.ivThumbnail)

    }

}