package com.kamil.tictactoe.grid

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.toolbox.Volley
import com.kamil.tictactoe.R
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.data.StateList
import com.kamil.tictactoe.data.buildStateList
import com.kamil.tictactoe.data.flattenOutState
import com.kamil.tictactoe.databinding.FragmentGridItemBinding
import com.kamil.tictactoe.services.GameAPI
import org.xmlpull.v1.XmlPullParser

class GridRecyclerViewAdapter(
    private val game: GameState,
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
            if (state[position] != 0) {
                return@setOnClickListener
            }

            // Set CROSS when checked
            it.background = holder.itemView.resources.getDrawable(R.color.white)
            it.foreground = holder.itemView.resources.getDrawable(R.drawable.cross_24)
            it.foregroundGravity = Gravity.CENTER

            // Build current state
            state[position] = 1

            // Prepare new game state object
            val updatedState = buildStateList(state)
            game.state = updatedState

            // Send data
            GameAPI.updateGame(Volley.newRequestQueue(holder.itemView.context), game) {
                Log.println(Log.VERBOSE, TAG, it.toString())
            }

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

    companion object {
        const val TAG = "GridRecyclerViewAdapter"
    }
}