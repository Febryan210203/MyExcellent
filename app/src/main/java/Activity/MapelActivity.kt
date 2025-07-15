package Activity

import Adapter.MapelAdapter
import ApiService.ApiClient
import Domain.Mapel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SmartTutor.databinding.ActivityKelasBinding
import kotlinx.coroutines.launch
import kotlin.collections.emptyList
import kotlin.collections.map



class MapelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKelasBinding
    private lateinit var adapter: MapelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MapelAdapter(emptyList())
        binding.kelasRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.kelasRecyclerView.adapter = adapter

        getDataFromApi()

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun getDataFromApi() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getMapel()
                if (response.isSuccessful && response.body()?.status == true) {
                    val apiData = response.body()?.data ?: emptyList()
                    Log.d("TAG", "getDataFromApi: $apiData")
                    // Mapping dari MapelApi ke Mapel (yang kamu pakai di adapter)
                    val mapelList = apiData.map {
                        Mapel(
                            nama_mapel = it.nama_mapel,
                            deskripsi = it.deskripsi ?: "Tidak ada deskripsi",
//                            image = R.drawable.mtk, // default gambar
                            jenjang = it.jenjang,
                            id_layanan = it.id_layanan,
                            id_mapel = it.id_mapel,
                            kode_mapel = it.kode_mapel,
                            status = it.status,
                            nama_layanan = it.nama_layanan

                        )
                    }

                    adapter.updateData(mapelList)
                } else {
                    Toast.makeText(this@MapelActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MapelActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
