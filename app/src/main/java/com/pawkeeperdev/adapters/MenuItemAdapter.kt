package com.pawkeeperdev.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pawkeeperdev.R

class MenuItemAdapter(private val listOfBtn : ArrayList<String>, private val listener : OnItemClickListener) : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview_menu_recycler,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listOfBtn.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listOfBtn[position]
        holder.tvBtnName.text = data
        holder.tvBtnName.setOnClickListener {
            listener.onItemClick(position)
        }
    }
    inner class ViewHolder(view : View): RecyclerView.ViewHolder(view){
        val tvBtnName : TextView =view.findViewById(R.id.tvBtnName)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}
