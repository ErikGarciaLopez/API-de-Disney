package com.example.apirestdisney.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apirestdisney.R
import com.example.apirestdisney.data.remote.DisneyService
import com.example.apirestdisney.data.remote.model.Character
import com.example.apirestdisney.databinding.FragmentCharacterListBinding
import com.example.apirestdisney.util.Constants
import com.example.apirestdisney.view.adapters.CharacterAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CharacterListFragment : Fragment() {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!
    private lateinit var disneyService: DisneyService
    private lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarRetrofit()
        configurarRetryButton()
        cargarCharacters()
    }

    private fun configurarRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        disneyService = retrofit.create(DisneyService::class.java)
    }

    private fun configurarRetryButton() {
        binding.btnRetry.setOnClickListener {
            mostrarLoading()
            cargarCharacters()
        }
    }

    private fun cargarCharacters() {
        val call: Call<Character> = disneyService.getCharacterList(pageSize = 7438)

        call.enqueue(object : Callback<Character> {
            override fun onResponse(p0: Call<Character>, response: Response<Character>) {
                if (response.isSuccessful) {
                    manejarSuccessfulResponse(response)
                } else {
                    manejarErrorResponse()
                }
            }

            override fun onFailure(p0: Call<Character>, p1: Throwable) {
                manejarFailure(p1)
            }
        })
    }

    private fun manejarSuccessfulResponse(response: Response<Character>) {
        ocultarLoading()
        ocultarError()

        val characterList = response.body()?.data ?: emptyList()
        binding.apply {
            rvGames.layoutManager = LinearLayoutManager(requireActivity())
            rvGames.adapter = CharacterAdapter(characterList) { character ->
                character.id?.let { id ->
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CharacterDetailFragment.newInstance(id))
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    private fun manejarErrorResponse() {
        ocultarLoading()
        mostrarError(getString(R.string.error_loading_data))
    }

    private fun manejarFailure(throwable: Throwable) {
        ocultarLoading()
        mostrarError(getString(R.string.error_no_connection))
        Log.e(Constants.LOGTAG, "Error: ${throwable.message}")
    }

    private fun mostrarLoading() {
        binding.apply {
            pbLoading.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
            rvGames.visibility = View.GONE
        }
    }

    private fun ocultarLoading() {
        binding.pbLoading.visibility = View.GONE
    }

    private fun mostrarError(message: String) {
        binding.apply {
            errorLayout.visibility = View.VISIBLE
            tvError.text = message
            rvGames.visibility = View.GONE
        }
    }

    private fun ocultarError() {
        binding.apply {
            errorLayout.visibility = View.GONE
            rvGames.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}