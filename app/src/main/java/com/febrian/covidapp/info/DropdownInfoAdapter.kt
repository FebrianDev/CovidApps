package com.febrian.covidapp.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.febrian.covidapp.R
import com.febrian.covidapp.databinding.ItemDropdownInfoBinding
import com.febrian.covidapp.info.data.DataGeneral
import com.febrian.covidapp.info.data.Model

class DropdownInfoAdapter(listData : ArrayList<Model>) : RecyclerView.Adapter<DropdownInfoAdapter.ViewHolder>() {

    val list : ArrayList<Model> = listData

    class ViewHolder(private val binding : ItemDropdownInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(m : Model)
        {
            with(binding){
                binding.titleItem.text = m.title.toString()
                binding.descriptionItem.text = m.description.toString()

                binding.cardItem.setOnClickListener {
                  act(binding)
                }

                binding.icon.setOnClickListener {
                    act(binding)
                }
            }
        }


        private fun act(binding: ItemDropdownInfoBinding){
            if (binding.detailExpand.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(
                    binding.cardItem,
                    AutoTransition()
                )
                binding.icon.setBackgroundResource(R.drawable.ic_baseline_add_circle_outline_24)
                binding.detailExpand.visibility = View.GONE

            } else {
                TransitionManager.beginDelayedTransition(
                    binding.cardItem,
                    AutoTransition()
                )
                binding.icon.setBackgroundResource(R.drawable.ic_baseline_remove_circle_outline_24)
                binding.detailExpand.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DropdownInfoAdapter.ViewHolder {
        val view = ItemDropdownInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DropdownInfoAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}