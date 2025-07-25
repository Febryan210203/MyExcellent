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
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding


    private lateinit var adapter: HistoryAdapter
    private val riwayatList = ArrayList<HistoryModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HistoryAdapter(riwayatList)
        binding.recyclerViewRiwayat.adapter = adapter
        binding.recyclerViewRiwayat.layoutManager = LinearLayoutManager(requireContext())

        fetchHistory()

        // Set click listener untuk tombol tambah
        binding.btnTambah.setOnClickListener {
            navigateToPemesananFragment()
        }
    }

    private fun navigateToPemesananFragment() {
        val pemesananFragment = PemesananFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, pemesananFragment) // Ganti dengan ID container yang sesuai
            .addToBackStack(null) // Tambahkan ke back stack agar bisa kembali
            .commit()
    }

    private fun fetchHistory() {
        val sharedPref = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        Log.d("SHARED_PREF", "ID Pelajar: ${sharedPref.getString("id_pelajar", "NOT_FOUND")}")
        val idPelajar = sharedPref.getString("id_pelajar", "") ?: ""

        ApiClient.instance.getPermohonanDisetujui(idPelajar, "disetujui")
            .enqueue(object : retrofit2.Callback<DisetujuiResponse> {
                override fun onResponse(
                    call: Call<DisetujuiResponse>,
                    response: Response<DisetujuiResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val data = response.body()?.data ?: emptyList()
                        riwayatList.clear()
                        Log.d("FETCH_HISTORY", "ID Pelajar: $idPelajar")
                        Log.d("FETCH_HISTORY", "Jumlah data: ${data.size}")

                        riwayatList.addAll(
                            data.map {
                                HistoryModel(
                                    id_permohonan = it.idPermohonan,
                                    id_pelajar = it.idPelajar,
                                    id_mapel = it.idMapel,
                                    id_layanan = it.idLayanan,
                                    id_guru = it.idGuru,
                                    status_permohonan = it.statusPermohonan,
                                    tanggal_mulai = it.tanggalMulai,
                                    created_at = it.createdAt,
                                    updated_at = it.updatedAt,
                                    deleted_at = it.deletedAt?.toString(),
                                    nama_guru = it.namaGuru,
                                    nama_pelajar = it.namaPelajar,
                                    nama_mapel = it.namaMapel,
                                    jenjang = it.jenjang,
                                    nama_layanan = it.namaLayanan,
                                    file = it.file
                                )
                            }
                        )

                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("FETCH_HISTORY", "Gagal: ${response.errorBody()?.string()}")
                        Toast.makeText(requireContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DisetujuiResponse>, t: Throwable) {
                    Log.e("FETCH_HISTORY", "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}

