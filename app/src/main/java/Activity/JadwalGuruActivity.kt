package Activity

import Adapter.JadwalGuruAdapter
import ApiService.ApiClient
import DataJson.JadwalResponse
import Domain.JadwalModel
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SmartTutor.databinding.ActivityJadwalMapelBinding
import retrofit2.Call
import retrofit2.Response

class JadwalGuruActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJadwalMapelBinding
    private lateinit var adapter: JadwalGuruAdapter
    private var jadwalList = mutableListOf<JadwalModel>()
    private var filteredJadwalList = mutableListOf<JadwalModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJadwalMapelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        adapter = JadwalGuruAdapter(filteredJadwalList) { jadwal ->
            // Aksi ketika item diklik
            val intent = Intent(this, MapelActivity::class.java)
            intent.putExtra("namaMapel", jadwal.nama_mapel)
            intent.putExtra("hari", jadwal.hari)
            intent.putExtra("waktu", "${jadwal.jam_mulai} - ${jadwal.jam_selesai}")
            intent.putExtra("guru", jadwal.nama_guru)
            startActivity(intent)
        }

        binding.recyclerViewJadwal.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewJadwal.adapter = adapter

        // Panggil API
        fetchJadwal()

        // Filter Search
        binding.etSearchJadwal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim().lowercase()
                filteredJadwalList = if (query.isEmpty()) {
                    jadwalList.toMutableList()
                } else {
                    jadwalList.filter {
                        it.nama_mapel.lowercase().contains(query) ||
                                it.hari.lowercase().contains(query) ||
                                it.nama_guru.lowercase().contains(query)
                    }.toMutableList()
                }
                adapter.updateList(filteredJadwalList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchJadwal() {
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
                        adapter.updateList(filteredJadwalList)
                        Log.d("JadwalMapelActivity", "Data dikirim ke adapter")

                        }
                    } else {
                    Log.e("JadwalMapelActivity", "Response tidak sukses")
                }
                }

            override fun onFailure(call: Call<JadwalResponse>, t: Throwable) {
                Log.e("JadwalMapelActivity", "fetchJadwal error!", t)
            }

        })
    }
}
