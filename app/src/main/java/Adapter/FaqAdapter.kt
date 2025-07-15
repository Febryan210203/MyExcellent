package Adapter

import Domain.FaqModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SmartTutor.databinding.ItemFaqBinding

class FaqAdapter(private var faqList: List<FaqModel>) :
    RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {
    inner class FaqViewHolder(val binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val item = faqList[position]
        holder.binding.txtQuestion.text = item.question
        holder.binding.txtAnswer.text = item.answer
    }

    override fun getItemCount(): Int = faqList.size

    fun updateList(newList: List<FaqModel>) {
        faqList = newList
        notifyDataSetChanged()
    }
}