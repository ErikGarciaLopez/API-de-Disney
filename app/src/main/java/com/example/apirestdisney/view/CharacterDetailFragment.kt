package com.example.apirestdisney.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apirestdisney.data.remote.DisneyService
import com.example.apirestdisney.data.remote.model.CharacterDetail
import com.example.apirestdisney.databinding.FragmentCharacterDetailBinding
import com.example.apirestdisney.util.Constants
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ARG_ID = "id"

class CharacterDetailFragment : Fragment() {

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ARG_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val disneyService = retrofit.create(DisneyService::class.java)

        //val call = disneyService.getCharacterDetail(id)
        val call = disneyService.getCharacterDetail(id!!)

        call.enqueue(object: Callback<CharacterDetail> {
            override fun onResponse(call: Call<CharacterDetail>, response: Response<CharacterDetail>) {
                Log.d(Constants.LOGTAG, "URL llamada: ${call.request().url}")

                if (response.isSuccessful) {
                    val characterDetail = response.body()
                    Log.d(Constants.LOGTAG, "Respuesta completa: ${response.body()}")

                    characterDetail?.data?.let { data ->
                        Log.d(Constants.LOGTAG, "ID del personaje: ${data.id}")
                        Log.d(Constants.LOGTAG, "Nombre del personaje: ${data.name}")

                        binding.apply {
                            pbLoading.visibility = View.INVISIBLE
                            tvTitle.text = data.name ?: "Nombre no disponible"

                            Picasso.get()
                                .load(data.imageUrl)
                                .into(binding.ivImage)

                            tvListFilms.text = data.films?.joinToString(separator = "\n") ?: "Películas no disponibles"

                        }
                    }
                } else {
                    Log.e(Constants.LOGTAG, "Error body: ${response.errorBody()?.string()}")
                    Log.e(Constants.LOGTAG, "Error code: ${response.code()}")
                    binding.pbLoading.visibility = View.INVISIBLE
                }
            }


            override fun onFailure(call: Call<CharacterDetail>, t: Throwable) {
                binding.pbLoading.visibility = View.INVISIBLE
                Log.e(Constants.LOGTAG, "Error de conexión: ${t.message}")
            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic fun newInstance(id: Int) =
                CharacterDetailFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_ID, id)
                    }
                }
    }
}