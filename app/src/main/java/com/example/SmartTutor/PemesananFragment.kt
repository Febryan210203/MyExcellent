package com.example.SmartTutor

import Activity.DetailPembokinganActivity
import Adapter.ItemGuruAdapter
import ApiService.ApiClient
import DataJson.GuruResponse
import DataJson.JadwalResponse
import DataJson.LayananResponse
import Domain.GuruModel
import Domain.JadwalModel
import Domain.LayananModel
import Domain.Mapel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.SmartTutor.databinding.FragmentPemesananBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class PemesananFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentPemesananBinding? = null
    private val binding get() = _binding!!
    private var allGuruList: List<GuruModel> = emptyList()
    private lateinit var adapter: ItemGuruAdapter
    private var jadwalList = mutableListOf<JadwalModel>()
    private var filteredJadwalList = mutableListOf<JadwalModel>()
    private var layananList: List<LayananModel> = emptyList()

    private var mapelList: List<Mapel> = emptyList()
    private var idMapel: String = ""
    private var idGuru: String = ""
    private var idJadwalGuru: String = ""
    private var idMapelFromMapel: String = ""
    private var idGuruFromGuru: String = ""
    private var idJadwalGuruFromJadwalGuru = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPemesananBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// Gets the data from the passed bundle
        val bundle = arguments
        if (bundle != null) {
            val cekIdMapel = bundle!!.getString("statusmapel")
            val cekIdGuru = bundle!!.getString("statusguru")
            val cekIdJadwalGuru = bundle!!.getString("statusjadwalguru")
            idMapelFromMapel = bundle!!.getString("id_mapel").toString()
            idGuruFromGuru = bundle!!.getString("id_guru").toString()
            idJadwalGuruFromJadwalGuru = bundle!!.getString("id_jadwalguru").toString()


            Log.d("CEK IDMAPEL", "onViewCreated: $idMapel")
            Log.d("CEK IDGURU", "onViewCreated: $idGuru")
            Log.d("CEK IDJADWALGURU", "onViewCreated: $idJadwalGuru")
            Log.d("CEK STATUS MAPEL", "onViewCreated: $cekIdMapel")
            Log.d("CEK STATUS GURU", "onViewCreated: $cekIdGuru")
            Log.d("CEK STATUS JADWALGURU", "onViewCreated: $cekIdJadwalGuru")
        }


        adapter = ItemGuruAdapter(mutableListOf())


        // Tombol kembali ke HomeActivity
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        fetchingMapel()
        fetchingJenjang()
        fetchingLayanan()
        fetchingGuru()
        fetchingJadwalGuru()
        // Tanggal booking (DatePicker)
        pilihTanggal()

        // Tombol Booking ke DetailPembokinganActivity
        goToDetail()
    }


    private fun fetchingJenjang() {
        val list = arrayListOf(
            "SD",
            "SMP",
            "SMA"

        )
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_list, list)
        binding.spinnerJenjang.adapter = arrayAdapter

    }

    private fun fetchingLayanan() {
        ApiClient.instance.getLayanan().enqueue(object : Callback<LayananResponse> {
            override fun onResponse(
                call: Call<LayananResponse>,
                response: Response<LayananResponse>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    layananList = response.body()?.data ?: emptyList()
                    Log.d("layananlist", "namaLayanan: $layananList ")
                }
                val spinnerLayanan = layananList.map { it.nama_layanan }
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.spinner_list, spinnerLayanan)
                binding.spinnerNamaLayanan.adapter = arrayAdapter
            }

            override fun onFailure(call: Call<LayananResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun fetchingJadwalGuru() {
        ApiClient.instance.getJadwalModel().enqueue(object : retrofit2.Callback<JadwalResponse> {
            override fun onResponse(
                call: Call<JadwalResponse>,
                response: Response<JadwalResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        if (res.status) {
                            Log.d(
                                "JadwalMapelActivity",
                                "Jumlah jadwal diterima = ${res.data.size}"
                            )

                            // Mapping response ke jadwalList
                            val listBaru = res.data.map { item ->
                                JadwalModel(
                                    item.id_jadwal_guru,
                                    item.hari,
                                    item.jam_mulai,
                                    item.jam_selesai,
                                    item.id_guru,
                                    item.id_mapel,
                                    item.deleted_at,
                                    item.status,
                                    item.created_at,
                                    item.updated_at,
                                    item.nama_guru,
                                    item.foto_guru,
                                    item.email_guru,
                                    item.nama_mapel,
                                    item.id_layanan,
                                    item.nama_layanan
                                )
                            }
                            jadwalList.clear()
                            jadwalList.addAll(listBaru)

                        }
                        filteredJadwalList = jadwalList.toMutableList()
//                            adapter.updateList(filteredJadwalList)
//                            Log.d("JadwalMapelActivity", "Data dikirim ke adapter")

                    }
                } else {
//                        Log.e("JadwalMapelActivity", "Response tidak sukses")
                }
                val spinnerJadwalMapel = jadwalList.map { it.hari }
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.spinner_list, spinnerJadwalMapel)
                binding.spinnerJadwal.adapter = arrayAdapter
                // ✅ AUTO SELECT jika idJadwalGuruFromJadwalGuru tidak kosong
                if (!idJadwalGuruFromJadwalGuru.isNullOrEmpty()) {
                    val index = jadwalList.indexOfFirst { it.id_jadwal_guru == idJadwalGuruFromJadwalGuru }
                    if (index != -1) {
                        binding.spinnerJadwal.setSelection(index)
                        Log.d("SPINNER SELECTED", "Dipilih posisi ke-$index dengan id ${idJadwalGuruFromJadwalGuru}")
                    } else {
                        Log.d("SPINNER SELECTED", "id_mapel tidak ditemukan dalam list")
                    }
                }
            }

            override fun onFailure(call: Call<JadwalResponse>, t: Throwable) {
//                    Log.e("JadwalMapelActivity", "fetchJadwal error!", t)
            }

        })

    }


    private fun fetchingGuru() {
        ApiClient.instance.getGuruModel().enqueue(object : Callback<GuruResponse> {
            override fun onResponse(call: Call<GuruResponse>, response: Response<GuruResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    allGuruList = response.body()?.data ?: emptyList()
                    Log.d("GuruActivity", "Jumlah data guru: ${allGuruList}")
                    adapter.updateData(allGuruList)
                } else {
                    // Log.e("GuruActivity", "Gagal mendapatkan data: ${response.message()}")
                    // Toast.makeText(this@GuruActivity, "Gagal mendapatkan data dari server", Toast.LENGTH_SHORT).show()
                }
                val spinnerGuru = allGuruList.map { it.nama_guru }
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.spinner_list, spinnerGuru)
                binding.spinnerGuru.adapter = arrayAdapter
                // ✅ AUTO SELECT jika idGuruFromGuru tidak kosong
                if (!idGuruFromGuru.isNullOrEmpty()) {
                    val index = allGuruList.indexOfFirst { it.id_guru == idGuruFromGuru }
                    if (index != -1) {
                        binding.spinnerGuru.setSelection(index)
                        Log.d("SPINNER SELECTED", "Dipilih posisi ke-$index dengan id ${idGuruFromGuru}")
                    } else {
                        Log.d("SPINNER SELECTED", "id_guru tidak ditemukan dalam list")
                    }
                }

                setupNamaGuruListener()
            }

            override fun onFailure(call: Call<GuruResponse>, t: Throwable) {
                // Toast.makeText(this@GuruActivity, "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
                //  Log.e("GuruActivity", "Kesalahan API: ${t.message}")
            }
        })
    }

    private fun setupNamaGuruListener() {
        binding.spinnerGuru.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGuru = allGuruList[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }


    private fun fetchingMapel() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getMapel()
                if (response.isSuccessful && response.body()?.status == true) {
                    mapelList = response.body()?.data.orEmpty().map {
                        Mapel(
                            nama_mapel = it.nama_mapel,
                            deskripsi = it.deskripsi ?: "Tidak ada deskripsi",
                            jenjang = it.jenjang,
                            id_layanan = it.id_layanan,
                            id_mapel = it.id_mapel,
                            kode_mapel = it.kode_mapel,
                            status = it.status,
                            nama_layanan = it.nama_layanan
                        )
                    }

                    val spinnerMapel = mapelList.map { it.nama_mapel }

                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.spinner_list,
                        spinnerMapel
                    )
                    binding.spinnerMapel.adapter = adapter
                    // ✅ AUTO SELECT jika idMapelFromMapel tidak kosong
                    if (!idMapelFromMapel.isNullOrEmpty()) {
                        val index = mapelList.indexOfFirst { it.id_mapel == idMapelFromMapel }
                        if (index != -1) {
                            binding.spinnerMapel.setSelection(index)
                            Log.d("SPINNER SELECTED", "Dipilih posisi ke-$index dengan id ${idMapelFromMapel}")
                        } else {
                            Log.d("SPINNER SELECTED", "id_mapel tidak ditemukan dalam list")
                        }
                    }


                    setupNamaMapelListener()
                }
            } catch (e: Exception) {
                Log.e("FETCH MAPEL", "Gagal: ${e.message}")
            }
        }
    }

    private fun setupNamaMapelListener() {
        binding.spinnerMapel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedMapel = mapelList[position] // Ambil dari list asli
                idMapel = selectedMapel.id_mapel

                Log.d("SPINNER MAPEL", "Nama: ${selectedMapel.nama_mapel}, ID: $idMapel")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun goToDetail() {
        binding.btnBooking.setOnClickListener {
            val selectedNamaMapel = binding.spinnerMapel.selectedItem.toString()
            val selectedJenjang = binding.spinnerJenjang.selectedItem.toString()
            val selectedLayanan = binding.spinnerNamaLayanan.selectedItem.toString()
            val selectedJadwal = binding.spinnerJadwal.selectedItem.toString()
            val selectedGuru = binding.spinnerGuru.selectedItem.toString()
            val tanggalMulai = binding.edtTanggalBooking.text.toString()

            if (tanggalMulai.isEmpty()) {
                Toast.makeText(requireContext(), "silahkan isi tanggal terlebih dahulu", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Ambil ID berdasarkan posisi terpilih di spinner
            val mapelId = idMapel
            val guruId = allGuruList[binding.spinnerGuru.selectedItemPosition].id_guru
            val layananId = layananList[binding.spinnerNamaLayanan.selectedItemPosition].id_layanan
            val jadwalId = jadwalList[binding.spinnerJadwal.selectedItemPosition].id_jadwal_guru

            Log.d("PemesananFragment", """
            Mapel: $selectedNamaMapel (id: $mapelId)
            Jenjang: $selectedJenjang
            Layanan: $selectedLayanan (id: $layananId)
            Jadwal: $selectedJadwal (id: $jadwalId)
            Guru: $selectedGuru (id: $guruId)
            Tanggal Booking: $tanggalMulai
        """.trimIndent())

            val intent = Intent(requireContext(), DetailPembokinganActivity::class.java).apply {
                putExtra("id_mapel", mapelId)
                putExtra("nama_mapel", selectedNamaMapel)

                putExtra("id_layanan", layananId)
                putExtra("nama_layanan", selectedLayanan)

                putExtra("id_jadwal", jadwalId)
                putExtra("jadwal", selectedJadwal)

                putExtra("id_guru", guruId)
                putExtra("nama_guru", selectedGuru)

                putExtra("jenjang", selectedJenjang)
                putExtra("tanggal_mulai", tanggalMulai)
            }

            startActivity(intent)
        }
    }


    private fun pilihTanggal() {
        binding.edtTanggalBooking.setOnClickListener {
            // 1. Isi tanggal booking otomatis hari ini
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val formattedDate = String.format("%02d-%02d-%04d", day, month + 1, year)
            binding.edtTanggalBooking.setText(formattedDate)

            // 2. Nonaktifkan interaksi user dengan tanggal (tidak bisa diubah)
            binding.edtTanggalBooking.isEnabled = false
            binding.edtTanggalBooking.isFocusable = false
            binding.edtTanggalBooking.isClickable = false
            binding.edtTanggalBooking.isCursorVisible = false

            // 3. Tidak perlu lagi memanggil pilihTanggal()
            // pilihTanggal() // hapus atau dikomentari
        }
    }
            override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}