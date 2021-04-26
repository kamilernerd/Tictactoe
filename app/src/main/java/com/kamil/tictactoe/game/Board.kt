package com.kamil.tictactoe.game

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.data.flattenOutState
import com.kamil.tictactoe.databinding.FragmentGridListBinding
import com.kamil.tictactoe.dialogs.JoinGameDialog.Companion.JSON_RESPONSE
import com.kamil.tictactoe.grid.GridRecyclerViewAdapter

class Board : AppCompatActivity() {

    private lateinit var binding: FragmentGridListBinding

    private var data: GameState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentGridListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getParcelableExtra(JSON_RESPONSE)

        val gridLayoutManager = GridLayoutManager(this, 3)
        val gridRecyclerViewAdapter = GridRecyclerViewAdapter(flattenOutState(data!!.state))

        binding.gridLayout.layoutManager = gridLayoutManager
        binding.gridLayout.adapter = gridRecyclerViewAdapter
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        // Check if player is in game and rejoin the game
    }

    override fun onStop() {
        super.onStop()
        // Check if player is in game and forfeit the game
    }

    companion object {
        val TAG = "BoardActivity"
    }

}