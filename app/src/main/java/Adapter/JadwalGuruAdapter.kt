package Adapter

import Activity.DetailJadwalGuruActivity
import Domain.JadwalModel
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SmartTutor.databinding.ItemJadwalMapelBinding

class JadwalGuruAdapter(
    private var jadwalList: List<JadwalModel>,
    private val onItemClick: (JadwalModel) -> Unit
) : RecyclerView.Adapter<JadwalGuruAdapter.JadwalMapelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalMapelViewHolder {
        val binding = ItemJadwalMapelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JadwalMapelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JadwalMapelViewHolder, position: Int) {
        val jadwal = jadwalList[position]
        holder.bind(jadwal)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailJadwalGuruActivity::class.java).apply {
                putExtra("nama_mapel", jadwal.nama_mapel)
                putExtra("hari", jadwal.hari)
                putExtra("jam", "${jadwal.jam_mulai} - ${jadwal.jam_selesai}")
                putExtra("nama_guru", jadwal.nama_guru)
                putExtra("id_jadwalguru", jadwal.id_jadwal_guru)
                putExtra("email_guru", jadwal.email_guru)
                putExtra("foto_guru", jadwal.foto_guru)
                putExtra("nama_layanan", jadwal.nama_layanan)
            }
            context.startActivity(intent)
        }
    }



    override fun getItemCount(): Int {
        return jadwalList.size
    }

    // Method untuk memperbarui data ketika pencarian dilakukan
    fun updateList(newList: List<JadwalModel>) {
        jadwalList = newList
        notifyDataSetChanged()
    }

    inner class JadwalMapelViewHolder(
        private val binding: ItemJadwalMapelBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(jadwal: JadwalModel) {
            Log.d("AdapterBind", "Nama Mapel: ${jadwal.nama_mapel}, ${jadwal.hari}")

            binding.textMapel.text = jadwal.nama_mapel
            binding.textHari.text = jadwal.hari
            binding.textJenjang.text = "${jadwal.jam_mulai} - ${jadwal.jam_selesai}" // tampil waktu
            binding.textGuru.text = jadwal.nama_guru


        }
    }
}
