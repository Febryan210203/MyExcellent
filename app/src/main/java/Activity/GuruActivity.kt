package Activity

import Adapter.ItemGuruAdapter
import ApiService.ApiClient
import DataJson.GuruResponse
import Domain.GuruModel
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SmartTutor.databinding.ActivityGuruBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.SmartTutor.R

class GuruActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuruBinding
    private var allGuruList: List<GuruModel> = emptyList()
    private lateinit var adapter: ItemGuruAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuruBinding.inflate(layoutInflater)
        val searchText = binding.searchViewGuru.findViewById<AutoCompleteTextView>(
            androidx.appcompat.R.id.search_src_text
        )

        searchText.hint = "Silakan cari guru Anda"
        searchText.setHintTextColor(ContextCompat.getColor(this, R.color.black)) // atau putih jika background gelap
        setContentView(binding.root)

        // Inisialisasi adapter dengan list kosong
        adapter = ItemGuruAdapter(mutableListOf())
        binding.recyclerViewGuru.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewGuru.adapter = adapter




        // Ambil data dari API dan pasang ke adapter
        fetchDataGuru()

        // Setup pencarian
        setupSearch()
    }

    private fun fetchDataGuru() {
        ApiClient.instance.getGuruModel().enqueue(object : Callback<GuruResponse> {
            override fun onResponse(call: Call<GuruResponse>, response: Response<GuruResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    allGuruList = response.body()?.data ?: emptyList()
                    Log.d("GuruActivity", "Jumlah data guru: ${allGuruList}")
                    adapter.updateData(allGuruList)
                } else {
                    Log.e("GuruActivity", "Gagal mendapatkan data: ${response.message()}")
                    Toast.makeText(this@GuruActivity, "Gagal mendapatkan data dari server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GuruResponse>, t: Throwable) {
                Toast.makeText(this@GuruActivity, "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("GuruActivity", "Kesalahan API: ${t.message}")
            }
        })
    }

    private fun setupSearch() {
        binding.searchViewGuru.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // tidak dipakai
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filtered(newText.orEmpty()) // <-- ini WAJIB ada!
                return true
            }

        })
    }
}
