package com.febrian.covidapp.info

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febrian.covidapp.R
import com.febrian.covidapp.databinding.ItemListInfoBinding
import com.febrian.covidapp.info.data.ModelList

class ListInfoAdapter(listData: ArrayList<ModelList>) :
    RecyclerView.Adapter<ListInfoAdapter.ViewHolder>() {

    val list: ArrayList<ModelList> = listData

    companion object{
        const val KEY_TITLE_INFO = "KEY_TITLE_INFO"
        const val KEY_DESCRIPTION = "KEY_DESCRIPTION"
        const val KEY_IMAGE_INFO = "KEY_IMAGE_INFO"
    }

    class ViewHolder(private val binding: ItemListInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(m: ModelList) {
            with(binding) {
                binding.titleItem.text = m.title.toString()
                binding.description.text = m.description.toString()
                Glide.with(itemView.context).load(m.image).into(binding.imageItem)

                itemView.setOnClickListener {

                    val builder = AlertDialog.Builder(itemView.context)
                    val l_view = LayoutInflater.from(itemView.context).inflate(R.layout.alert_dialog_item_list_info,null)
                    builder.setView(l_view)

                    val dialog = builder.create()
                    dialog.show()
                    dialog.setCancelable(false)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                    val title = l_view.findViewById<TextView>(R.id.title)
                    val description = l_view.findViewById<TextView>(R.id.description)
                    val image = l_view.findViewById<ImageView>(R.id.image)
                    Glide.with(itemView.context).load(m.image).into(image)
                    title.text = m.title
                    description.text = m.description
                    val btnClose = l_view.findViewById<AppCompatButton>(R.id.btn_close)
                    btnClose.setOnClickListener {
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListInfoAdapter.ViewHolder {
        val view = ItemListInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListInfoAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListInfoAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}