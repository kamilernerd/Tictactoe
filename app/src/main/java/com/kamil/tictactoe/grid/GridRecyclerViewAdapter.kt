package com.kamil.tictactoe.grid

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.kamil.tictactoe.R
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.data.StateList
import com.kamil.tictactoe.data.buildStateList
import com.kamil.tictactoe.data.flattenOutState
import com.kamil.tictactoe.databinding.FragmentGridItemBinding
import com.kamil.tictactoe.services.GameAPI
import com.kamil.tictactoe.services.PLAYER

enum class ITEM_TYPE {
    EMPTY,
    CROSS,
    CIRCLE
}

class GridRecyclerViewAdapter(
    private val parentActivity: AppCompatActivity,
    private var game: GameState,
    private val IS_HOST: Boolean
) : RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder>() {

    private var state: MutableList<Int>? = null

    inner class ViewHolder(binding: FragmentGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        state = flattenOutState(game.state)

        val item = state!![position]
        holder.idView.text = item.toString()

        drawForegroundIcon(item, holder)

        holder.itemView.setOnClickListener {
            if (state!![position] != ITEM_TYPE.EMPTY.ordinal) {
                return@setOnClickListener
            }

            // Set cross
            holder.itemView.foreground = getDrawable(ITEM_TYPE.CROSS)
            holder.itemView.foregroundGravity = Gravity.CENTER

            val updatedState = buildCurrentState(state!!, position)

            // Send data
            GameAPI.updateGame(Volley.newRequestQueue(holder.itemView.context), game, updatedState) {
                Log.println(Log.VERBOSE, TAG, it.toString())
            }
        }
    }

    private fun buildCurrentState(state: MutableList<Int>, position: Int): StateList {
        if (IS_HOST) {
            state[position] = ITEM_TYPE.CROSS.ordinal
        } else {
            state[position] = ITEM_TYPE.CIRCLE.ordinal
        }
        return buildStateList(state)
    }

    private fun drawForegroundIcon(item: Int, holder: ViewHolder) {
        if (IS_HOST) {
            if (item == ITEM_TYPE.CROSS.ordinal) {
                holder.itemView.foreground = getDrawable(ITEM_TYPE.CROSS)
                holder.itemView.foregroundGravity = Gravity.CENTER
            } else if (item == ITEM_TYPE.CIRCLE.ordinal) {
                holder.itemView.foreground = getDrawable(ITEM_TYPE.CIRCLE)
                holder.itemView.foregroundGravity = Gravity.CENTER
            }
        } else {
            if (item == ITEM_TYPE.CIRCLE.ordinal) {
                holder.itemView.foreground = getDrawable(ITEM_TYPE.CROSS)
                holder.itemView.foregroundGravity = Gravity.CENTER
            } else if (item == ITEM_TYPE.CROSS.ordinal) {
                holder.itemView.foreground = getDrawable(ITEM_TYPE.CIRCLE)
                holder.itemView.foregroundGravity = Gravity.CENTER
            }
        }
    }

    private fun getDrawable(type: ITEM_TYPE): Drawable? {
        if (type == ITEM_TYPE.CROSS) {
            return parentActivity.getDrawable(R.drawable.cross_24)
        } else if (type == ITEM_TYPE.CIRCLE) {
            return parentActivity.getDrawable(R.drawable.knots)
        }
        return null
    }

    override fun getItemCount(): Int = flattenOutState(game.state).size

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