package com.example.apirestdisney.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apirestdisney.R
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
    private lateinit var disneyService: DisneyService
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ARG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()
        configRetryButton()
        cargarCharacterDetail()
    }

    private fun setupRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        disneyService = retrofit.create(DisneyService::class.java)
    }

    private fun configRetryButton() {
        binding.btnRetry.setOnClickListener {
            mostrarLoading()
            cargarCharacterDetail()
        }
    }

    private fun cargarCharacterDetail() {
        id?.let { characterId ->
            val call = disneyService.getCharacterDetail(characterId)

            call.enqueue(object : Callback<CharacterDetail> {
                override fun onResponse(
                    call: Call<CharacterDetail>,
                    response: Response<CharacterDetail>
                ) {
                    Log.d(Constants.LOGTAG, "URL llamada: ${call.request().url}")

                    if (response.isSuccessful) {
                        manejarSuccessfulResponse(response.body())
                    } else {
                        manejarErrorResponse(response)
                    }
                }

                override fun onFailure(call: Call<CharacterDetail>, t: Throwable) {
                    manejarFailure(t)
                }
            })
        } ?: run {
            manejarError(getString(R.string.error_id_unavailable))
        }
    }

    private fun manejarSuccessfulResponse(characterDetail: CharacterDetail?) {
        characterDetail?.data?.let { data ->
            Log.d(Constants.LOGTAG, "ID del personaje: ${data.id}")
            Log.d(Constants.LOGTAG, "Nombre del personaje: ${data.name}")

            ocultarLoading()
            ocultarError()
            mostrarContenido()

            binding.apply {
                tvTitle.text = data.name ?: getString(R.string.error_name_unavailable)
                tvListFilms.text = data.films?.joinToString(separator = "\n") ?: getString(R.string.films_unavailable)

                Picasso.get()
                    .load(data.imageUrl)
                    .placeholder(R.drawable.default_character)
                    .error(R.drawable.default_character)
                    .into(ivImage)
            }
        } ?: manejarError(getString(R.string.error_character_data_unavailable))
    }

    private fun manejarErrorResponse(response: Response<CharacterDetail>) {
        Log.e(Constants.LOGTAG, "Error body: ${response.errorBody()?.string()}")
        Log.e(Constants.LOGTAG, "Error code: ${response.code()}")

        val errorMessage = when (response.code()) {
            404 -> getString(R.string.error_character_not_found)
            500 -> getString(R.string.error_server)
            else -> getString(R.string.error_loading_data_2)
        }
        manejarError(errorMessage)
    }

    private fun manejarFailure(t: Throwable) {
        Log.e(Constants.LOGTAG, "Error de conexión: ${t.message}")
        manejarError(getString(R.string.error_no_connection_short))
    }

    private fun manejarError(message: String) {
        ocultarLoading()
        ocultarContenido()
        mostrarError(message)
    }

    private fun mostrarLoading() {
        binding.apply {
            pbLoading.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
            scrollView.visibility = View.GONE
        }
    }

    private fun ocultarLoading() {
        binding.pbLoading.visibility = View.GONE
    }

    private fun mostrarError(message: String) {
        binding.apply {
            errorLayout.visibility = View.VISIBLE
            tvError.text = message
        }
    }

    private fun ocultarError() {
        binding.errorLayout.visibility = View.GONE
    }

    private fun mostrarContenido() {
        binding.scrollView.visibility = View.VISIBLE
    }

    private fun ocultarContenido() {
        binding.scrollView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
            CharacterDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ID, id)
                }
            }
    }
}