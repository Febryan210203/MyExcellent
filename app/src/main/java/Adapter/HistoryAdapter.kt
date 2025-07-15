package Adapter

import Activity.DetailPembokinganActivity
import Activity.DetailRiwayatPemesananActivity
import HistoryModel
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SmartTutor.databinding.ItemHistoryBinding

class HistoryAdapter(private val list: List<HistoryModel>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // Gunakan field yang benar dari HistoryModel
        holder.binding.tvMapel.text = "nama mapel: ${item.nama_mapel}"
        holder.binding.tvGuru.text = "nama guru: ${item.nama_guru}"
        holder.binding.tvTanggal.text = "tanggal mulai: ${item.tanggal_mulai}"
        holder.binding.tvJenis.text = "layanan: ${item.nama_layanan}"
        holder.binding.tvHarga.text = "150.000" // Tambahkan jika ada field harga
        holder.binding.tvStatus.text = "Status: ${item.status_permohonan}"

        // Warna status
        val statusColor = when (item.status_permohonan.lowercase()) {
            "selesai" -> Color.parseColor("#388E3C")
            "ditolak", "dibatalkan" -> Color.parseColor("#D32F2F")
            else -> Color.parseColor("#FBC02D") // misalnya: menunggu
        }
        holder.binding.tvStatus.setTextColor(statusColor)

        // Navigasi ke halaman detail
        holder.binding.btnDetail.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailRiwayatPemesananActivity::class.java)
            intent.putExtra("nama_mapel", item.nama_mapel)
            intent.putExtra("nama_guru", item.nama_guru)
            intent.putExtra("jenjang", item.jenjang)
            intent.putExtra("nama_layanan", item.nama_layanan)
            intent.putExtra("tanggal_mulai", item.tanggal_mulai)
            // Tidak ada biaya, jadi sementara diset 0
            intent.putExtra("biaya", 150)
            context.startActivity(intent)
        }
    }
}
