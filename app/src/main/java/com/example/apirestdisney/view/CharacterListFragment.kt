package com.example.apirestdisney.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.apirestdisney.R
import com.example.apirestdisney.data.remote.DisneyService
import com.example.apirestdisney.data.remote.model.Character
import com.example.apirestdisney.databinding.FragmentCharacterListBinding
import com.example.apirestdisney.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class CharacterListFragment : Fragment() {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val disneyService = retrofit.create(DisneyService::class.java)

        val call: Call<Character> = disneyService.getCharacterList(pageSize = 7438)

        call.enqueue(object: Callback<Character>{
            override fun onResponse(p0: Call<Character>, response: Response<Character>) {
                binding.pbLoading.visibility = View.INVISIBLE

                Log.d(Constants.LOGTAG, response.toString())
                Log.d(Constants.LOGTAG, response.body().toString())
            }

            override fun onFailure(p0: Call<Character>, p1: Throwable) {
                binding.pbLoading.visibility = View.INVISIBLE
                //TODO: Manejarlo de otra forma
                Toast.makeText(
                    requireActivity(),
                    "No hay conexión disponible",
                    Toast.LENGTH_SHORT
                ).show()
                p1.message
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}