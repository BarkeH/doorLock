package com.example.testmulti.ui.home

import APIService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.testmulti.databinding.FragmentHomeBinding
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLock.setOnClickListener(){
            rawJSON(true)
        }
        binding.btnUnlock.setOnClickListener() {
            rawJSON(false)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun rawJSON(locked: Boolean) {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("locked", locked.toString())

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.sendRequest(requestBody)

            withContext(Dispatchers.Main) {
                //if (response.isSuccessful) {

                // Convert raw JSON to pretty JSON using GSON library
                //val gson = GsonBuilder().setPrettyPrinting().create()
                /*val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    )
                )

                Log.d("Pretty Printed JSON :", prettyJson)

            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())

            }*/
            }
        }
    }

}
