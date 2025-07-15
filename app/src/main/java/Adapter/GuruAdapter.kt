package Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import Domain.CardModel
import com.example.SmartTutor.databinding.ActivityItemGuruBinding

class GuruAdapter(private val context: Context, private val dataList: List<CardModel>) :
    RecyclerView.Adapter<GuruAdapter.GuruViewHolder>() {

    inner class GuruViewHolder(private val binding: ActivityItemGuruBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cardModel: CardModel) {
            binding.imageGuru.setImageResource(cardModel.imageRes)
            binding.textName.text = cardModel.name
            binding.textRating.text = cardModel.rating
            binding.textReview.text = cardModel.review
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuruViewHolder {
        val binding = ActivityItemGuruBinding.inflate(LayoutInflater.from(context), parent, false)
        return GuruViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuruViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
}
