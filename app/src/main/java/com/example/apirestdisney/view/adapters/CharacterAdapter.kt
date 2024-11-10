package com.example.apirestdisney.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apirestdisney.data.remote.model.Data
import com.example.apirestdisney.databinding.CharacterElementBinding

class CharacterAdapter(
    private val characters: List<Data>,
    private val onCharacterClick: (Data) -> Unit
):RecyclerView.Adapter<CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = CharacterElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.bind(character)

        holder.itemView.setOnClickListener {
            onCharacterClick(character)
        }

    }
}