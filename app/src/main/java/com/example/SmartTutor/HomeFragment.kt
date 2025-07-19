package com.example.SmartTutor

import Activity.EditProfileActivity
import Activity.GuruActivity
import Activity.JadwalGuruActivity
import Activity.MapelActivity
import Adapter.GuruAdapter
import ApiService.ApiClient
import DataJson.TokenResponse
import Domain.CardModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SmartTutor.databinding.FragmentHomeBinding
import com.google.firebase.messaging.FirebaseMessaging



class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val TAG = "FCM_TOKEN"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Ambil token FCM saat fragment dibuat
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(TAG, "Token FCM: $token")
//                sendTokenToServer(token)
                fetchingToken(token)

            } else {
                Log.e(TAG, "Gagal mendapatkan token FCM", task.exception)
            }
        }
        return binding.root
    }


    private fun sendTokenToServer(token: String?) {
        // Ambil token login dari SharedPreferences (kalau butuh autentikasi)
        val context = context ?: return
        val sharedPref = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userToken = sharedPref.getString("access_token", null)




        if (userToken == null) {
            Log.d("Token", "Token login tidak ditemukan")
            return
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Contoh penggunaan ViewBinding

        val sharedPref = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", "Email belum disimpan")
        val nama = sharedPref.getString("nama", "Nama belum diisi")
        val token = sharedPref.getString("access_token", null)

        Log.d("Token", "Access Token: $token")
        binding.txtViewEmail.text = email
        binding.textView6.text = nama

        binding.cardView01.setOnClickListener {
            val intent = Intent(requireContext(), MapelActivity::class.java)
            startActivity(intent)
        }
        binding.imageView6.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
//        binding.imageView7.setOnClickListener {
//            val intent = Intent(requireContext(), NotifikasiActivity::class.java)
//            startActivity(intent)
//        }

        binding.cardView02.setOnClickListener {
            val intent = Intent(requireContext(), GuruActivity::class.java)
            startActivity(intent)
        }

        binding.cardView03.setOnClickListener {
            val intent = Intent(requireContext(), JadwalGuruActivity::class.java)
            startActivity(intent)
        }

        val dataList = listOf(
            CardModel(
                R.drawable.guru1,
                "Bpk Andre",
                "4.8 :",
                "“Guru yang sangat profesional dan sabar dalam mengajar.”"
            ),
            CardModel(
                R.drawable.guru2,
                "Ibu Siti",
                "4.9 :",
                "“Guru yang sangat profesional dan sabar dalam mengajar.”"
            ),
            CardModel(
                R.drawable.guru3,
                "Ibu Indriyani",
                "4.5 :",
                "“Guru yang sangat profesional dan sabar dalam mengajar.”"
            ),
            CardModel(
                R.drawable.guru1,
                "Bapak Ridwan",
                "4.8",
                "”Guru yang sangat antusias dalam membimbing murid-murid.”"
            )
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = GuruAdapter(requireContext(), dataList)
        // Menggunakan dataList yang benar


        // Untuk RecyclerView nanti tinggal diatur adapter-nya
        // binding.recyclerView.adapter = ...
    }

    private fun fetchingToken(token: String?) {
        val context = context ?: return
        val sharedPref = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userToken = sharedPref.getString("access_token", null)




        if (userToken == null) {
            Log.d("Token", "Token login tidak ditemukan")
            return
        }
        val bearerToken = "119|tGecog44WLe3Vu7v6MPk1Jfq9saL24AkwcbgN6S43a6e4c69" // Ganti token ini dengan token login user sebenarnya

        ApiClient.instance.sendFcmToken("Bearer $userToken", token!!)
            .enqueue(object : retrofit2.Callback<TokenResponse> {
                override fun onResponse(call: retrofit2.Call<TokenResponse>, response: retrofit2.Response<TokenResponse>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.success == true) {
                            Log.d("SENDTOKEN", "Token berhasil disimpan: ${body.message}")
                        } else {
                            Log.d("BACKSEND", "Gagal simpan token: ${body?.message}")
                        }
                    } else {
                        Log.d("ERORSEND", "Respon error: ${response.code()} ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: retrofit2.Call<TokenResponse>, t: Throwable) {
                    Log.d("FCM", "Error koneksi: ${t.message}")
                }
            })

        Log.d("FCM_SEND", "Mengirim token ke server: $token")
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
