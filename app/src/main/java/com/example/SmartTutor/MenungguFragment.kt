package com.example.SmartTutor

import Adapter.HistoryAdapter
import ApiService.ApiClient
import DataJson.DisetujuiResponse
import HistoryModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SmartTutor.databinding.FragmentHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class MenungguFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private val riwayatList = ArrayList<HistoryModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryAdapter(riwayatList)
//        binding.recyclerViewRiwayat.adapter = adapter
//        binding.recyclerViewRiwayat.layoutManager = LinearLayoutManager(requireContext())

        fetchData()
    }

    private fun fetchData() {
        val sharedPref = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val idPelajar = sharedPref.getString("id_pelajar", "") ?: ""


        ApiClient.instance.getPermohonanDisetujui(idPelajar, "menunggu")
            .enqueue(object : Callback<DisetujuiResponse> {
                override fun onResponse(call: Call<DisetujuiResponse>, response: Response<DisetujuiResponse>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val data = response.body()?.data ?: emptyList()
                        riwayatList.clear()
                        Log.d("FETCH_HISTORY", "Jumlah data: ${data.size}")
                        riwayatList.addAll(data.filterNotNull().map {
                            HistoryModel(
                                it.idPermohonan ?: "",
                                it.idPelajar ?: "",
                                it.idMapel ?: "",
                                it.idLayanan ?: "",
                                it.idGuru ?: "",
                                it.statusPermohonan ?: "",
                                it.tanggalMulai ?: "",
                                it.createdAt ?: "",
                                it.updatedAt ?: "",
                                it.deletedAt?.toString(),
                                it.namaGuru ?: "",
                                it.namaPelajar ?: "",
                                it.namaMapel ?: "",
                                it.jenjang ?: "",
                                it.namaLayanan ?: ""
                            )
                        })
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DisetujuiResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
