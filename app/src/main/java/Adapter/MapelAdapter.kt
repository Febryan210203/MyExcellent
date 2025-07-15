package Adapter

import Activity.DetailMapelActivity
import Domain.Mapel
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.SmartTutor.databinding.ItemKelasBinding

class MapelAdapter(
    private var originalList: List<Mapel>
) : RecyclerView.Adapter<MapelAdapter.KelasViewHolder>(), Filterable {

    private var filteredList = originalList.toMutableList()

    inner class KelasViewHolder(val binding: ItemKelasBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KelasViewHolder {
        val binding = ItemKelasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KelasViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: KelasViewHolder, position: Int) {
        val kelas = filteredList[position]
        holder.binding.txtNamaMapel.text = kelas.nama_mapel
        holder.binding.txtDeskripsi.text = kelas.deskripsi
        holder.binding.labelJenjang.text = kelas.jenjang
//        holder.binding.imgKelas.setImageResource(kelas.image)

        holder.binding.cardItem.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailMapelActivity::class.java).apply {
                putExtra("nama_mapel", kelas.nama_mapel)
                putExtra("kode_mapel", kelas.kode_mapel)
                putExtra("deskripsi", kelas.deskripsi)
                putExtra("status", kelas.status)
                putExtra("jenjang", kelas.jenjang)
                putExtra("id_mapel", kelas.id_mapel)
                putExtra("nama_layanan", kelas.nama_layanan)
//                putExtra("image", kelas.image)
            }
            holder.itemView.context.startActivity(intent)
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
                        it.nama_mapel.lowercase().contains(filterQuery) ||
                                it.nama_layanan.lowercase().contains(filterQuery)||
                                it.deskripsi!!.lowercase().contains(filterQuery) ||
                                it.jenjang.lowercase().contains(filterQuery)

                    }
                }

                return FilterResults().apply {
                    values = filtered
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = (results?.values as List<Mapel>).toMutableList()
                notifyDataSetChanged()
            }
        }
    }

    fun updateData(newList: List<Mapel>) {
        originalList = newList
        filteredList = newList.toMutableList()
        notifyDataSetChanged()
    }
}
