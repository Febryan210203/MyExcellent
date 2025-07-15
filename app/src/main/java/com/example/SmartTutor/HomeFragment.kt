package com.example.SmartTutor

import Activity.EditProfileActivity
import Activity.GuruActivity
import Activity.JadwalGuruActivity
import Activity.MapelActivity
import Activity.NotifikasiActivity
import Adapter.GuruAdapter
import Domain.CardModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SmartTutor.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Contoh penggunaan ViewBinding

        val sharedPref = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", "Email belum disimpan")
        val nama = sharedPref.getString("nama","Nama belum diisi" )
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
        binding.imageView7.setOnClickListener {
            val intent = Intent(requireContext(), NotifikasiActivity::class.java)
            startActivity(intent)
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
