package com.example.apirestdisney.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

                //Log.d(Constants.LOGTAG, response.toString())
                //Log.d(Constants.LOGTAG, response.body().toString())
                val characterList = response.body()?.data ?: emptyList()  // Extraemos solo la lista de datos
                binding.apply {
                    rvGames.layoutManager = LinearLayoutManager(requireActivity())
                    rvGames.adapter = CharacterAdapter(characterList){ character ->
                        //Click al elemento
                        Log.d(Constants.LOGTAG, character.name)

                        character.id?.let{ id ->
                            //Mandar al segundo fragment el id
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, CharacterDetailFragment.newInstance(id))
                                .addToBackStack(null)
                                .commit()
                        }

                    }

                }

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