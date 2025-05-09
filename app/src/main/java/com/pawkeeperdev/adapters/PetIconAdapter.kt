package com.pawkeeperdev.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pawkeeperdev.R
import com.pawkeeperdev.databinding.ItemviewDogLogoBinding

class PetIconAdapter(
    private val context: Context,
    private val list: List<Int>,
    private val onClick: OnItemClick
) : RecyclerView.Adapter<PetIconAdapter.ViewHolder>() {

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemviewDogLogoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list.isNotEmpty()) {
            val drawable = ResourcesCompat.getDrawable(context.resources, list[position], null)
            Glide.with(context).load(drawable)
                .placeholder(R.color.white_light)
                .into(holder.binding.ivPetLogo)
        }

        holder.binding.ivPetLogo.setOnClickListener {
            if (position != selectedItemPosition) {
                notifyItemChanged(selectedItemPosition)
                selectedItemPosition = position
                notifyItemChanged(position)
                onClick.onClickIcon(position)
            }
        }

        holder.binding.ivPetLogo.borderColor =
            if (position == selectedItemPosition)
                context.getColor(R.color.color_primary_dark)
            else
                context.getColor(R.color.white)
    }

    inner class ViewHolder(val binding: ItemviewDogLogoBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClickIcon(position: Int)
    }
}
