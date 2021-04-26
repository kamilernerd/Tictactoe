package com.kamil.tictactoe.grid

import android.graphics.drawable.Drawable
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.kamil.tictactoe.R
import com.kamil.tictactoe.databinding.FragmentGridItemBinding
import org.xmlpull.v1.XmlPullParser

class GridRecyclerViewAdapter(
    private val state: MutableList<Int>
) : RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(binding: FragmentGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = state[position]
        holder.idView.text = item.toString()

        holder.itemView.setOnClickListener {
            // Set CROSS when checked
            it.background = holder.itemView.resources.getDrawable(R.color.white)
            it.foreground = holder.itemView.resources.getDrawable(R.drawable.cross_24)
            it.foregroundGravity = Gravity.CENTER

            // Build current state
            state[position] = 1

            // Send data


            // Start polling
        }
    }

    override fun getItemCount(): Int = state.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentGridItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }
}