package com.kamil.tictactoe.grid

import android.annotation.SuppressLint
import android.content.Context
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
import com.kamil.tictactoe.data.buildStateList
import com.kamil.tictactoe.databinding.FragmentGridItemBinding
import com.kamil.tictactoe.dialogs.GameEndedDialog
import com.kamil.tictactoe.services.GameAPI

enum class ITEM_TYPE {
    EMPTY,
    CROSS,
    CIRCLE
}

class GridRecyclerViewAdapter(
    private val parentContext: Context,
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

        // When 1 = cross, 2 = circle
        if (item == ITEM_TYPE.CROSS.ordinal) {
            holder.itemView.foreground = getDrawable(ITEM_TYPE.CROSS)
            holder.itemView.foregroundGravity = Gravity.CENTER
        } else if (item == ITEM_TYPE.CIRCLE.ordinal) {
            holder.itemView.foreground = getDrawable(ITEM_TYPE.CIRCLE)
            holder.itemView.foregroundGravity = Gravity.CENTER
        }

        holder.itemView.setOnClickListener {
            if (state[position] != ITEM_TYPE.EMPTY.ordinal) {
                return@setOnClickListener
            }

            // Set CROSS when checked
            it.foreground = getDrawable(ITEM_TYPE.CROSS)
            it.foregroundGravity = Gravity.CENTER

            // Build current state
            state[position] = ITEM_TYPE.CROSS.ordinal

            // Prepare new game state object
            val updatedState = buildStateList(state)
            game.state = updatedState

            GameAPI.checkGameState(game.state) { state, p1, p2 ->
                if (p1) {
                    Log.println(Log.VERBOSE, TAG, "$state, ${p1.toString()}")
//                    GameEndedDialog(
//                        "Congratulations you won!"
//                    ).show(
//                        ontext.resources.
//                    )
                } else if (p2) {
                    Log.println(Log.VERBOSE, TAG, "$state, ${p1.toString()}")
//                    GameEndedDialog(
//                        "Congratulations you won!"
//                    ).show(
//                        ontext.resources.
//                    )
                }
            }

            // Send data
            GameAPI.updateGame(Volley.newRequestQueue(holder.itemView.context), game) {
                // Start polling
                Log.println(Log.VERBOSE, TAG, it.toString())
            }
        }
    }

    fun getDrawable(type: ITEM_TYPE): Drawable? {
        if (type == ITEM_TYPE.CROSS) {
            return parentContext.getDrawable(R.drawable.cross_24)
        } else if (type == ITEM_TYPE.CIRCLE) {
            return parentContext.getDrawable(R.drawable.knots)
        }
        return null
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