package com.example.apirestdisney.view.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apirestdisney.data.remote.model.Character
import com.example.apirestdisney.data.remote.model.Data
import com.example.apirestdisney.databinding.CharacterElementBinding
import com.example.apirestdisney.databinding.FragmentCharacterListBinding
import com.example.apirestdisney.view.CharacterListFragment
import java.text.SimpleDateFormat
import java.util.Locale

class CharacterViewHolder(
    private val binding: CharacterElementBinding
) :RecyclerView.ViewHolder(binding.root) {

    fun bind(character: Data) {
        binding.tvTitle.text = character.name

        Glide.with(binding.root.context)
            .load(character.imageUrl)
            //TODO: poner .error(R.drawable.background_character) 1:34:30 y tambien placeholder
            .into(binding.ivThumbnail)

        binding.tvReleased.text = formatDate(character.createdAt)
    }

}

fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
    val date = inputFormat.parse(dateString)  // String a Date
    return date?.let { outputFormat.format(it) } ?: dateString  // Formatea
}