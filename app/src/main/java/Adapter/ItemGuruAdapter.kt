package Adapter

import Activity.DetailGuruActivity
import Domain.GuruModel
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ItemGuruBinding

class ItemGuruAdapter(private val guruList: MutableList<GuruModel>) :
    RecyclerView.Adapter<ItemGuruAdapter.GuruViewHolder>(), Filterable {

    private var originalList: MutableList<GuruModel> = mutableListOf()

    init {
        originalList.addAll(guruList)
    }

    inner class GuruViewHolder(val binding: ItemGuruBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuruViewHolder {
        val binding = ItemGuruBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuruViewHolder(binding)
    }

    override fun getItemCount(): Int = guruList.size

    override fun onBindViewHolder(holder: GuruViewHolder, position: Int) {
        val guruModel = guruList[position]
        with(holder.binding) {
            namaGuru.text = guruModel.nama_guru
            noHp.text = guruModel.no_hp
            noNip.text = guruModel.nip
            namaMapel.text = guruModel.nama_mapel



            // Load image with Glide
            val imageUrl = "https://smarttutor.desabinor.id/storage/${guruModel.foto}"
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.guru)
                .into(guruImage)

            cardItemGuru.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailGuruActivity::class.java).apply {
                    putExtra("namaGuru", guruModel.nama_guru)
                    putExtra("biodataGuru", guruModel.biodata)
                    putExtra("mapelGuru", guruModel.nama_mapel)
                    putExtra("noHpGuru", guruModel.no_hp)
                    putExtra("id_guru", guruModel.id_guru)
                    putExtra("nipGuru", guruModel.nip)
                    putExtra("alamatGuru", guruModel.alamat)
                    putExtra("pendidikanGuru", guruModel.pendidikan_terakhir)
                }
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val filterQuery = query.toString().lowercase()
                val filtered = if (filterQuery.isBlank()) {
                    originalList
                } else {
                    originalList.filter {
                        it.nama_guru.lowercase().contains(filterQuery) ||
                                it.biodata?.lowercase()?.contains(filterQuery) == true ||
                                it.nama_mapel.lowercase().contains(filterQuery)
                    }
                }

                val results = FilterResults()
                results.values = filtered
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                guruList.clear()
                if (results?.values is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    guruList.addAll(results.values as List<GuruModel>)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun updateData(newList: List<GuruModel>) {
        guruList.clear()
        guruList.addAll(newList)
        originalList.clear()
        originalList.addAll(newList)
        notifyDataSetChanged()
    }

    fun filtered(query: String) {
        guruList.clear()
        if (query.isEmpty()) {
            guruList.addAll(originalList)
        } else {
            val filtered = originalList.filter {
                it.nama_guru.contains(query, ignoreCase = true) ||
                        it.nama_mapel.contains(query, ignoreCase = true)
            }
            guruList.addAll(filtered)
        }
        notifyDataSetChanged()
    }
}
