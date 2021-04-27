package com.kamil.tictactoe.game

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.toolbox.Volley
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.data.flattenOutState
import com.kamil.tictactoe.databinding.FragmentGridListBinding
import com.kamil.tictactoe.grid.GridRecyclerViewAdapter
import com.kamil.tictactoe.services.GameAPI
import java.util.*


class Board : AppCompatActivity() {

    private lateinit var binding: FragmentGridListBinding

    private var data: GameState? = null
    private var IS_HOST: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentGridListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getParcelableExtra(JSON_RESPONSE)
        IS_HOST = intent.getBooleanExtra("IS_HOST", IS_HOST)

        val gridLayoutManager = GridLayoutManager(this, 3)
        val gridRecyclerViewAdapter = GridRecyclerViewAdapter(this, data!!, IS_HOST)

        binding.gameId.text = "Game code: ${data!!.gameId}"

        binding.gridLayout.layoutManager = gridLayoutManager
        binding.gridLayout.adapter = gridRecyclerViewAdapter

        GameAPI.pollDataTimer(this, data!!.gameId) { data: GameState, timer: Timer ->
            binding.gridLayout.adapter = GridRecyclerViewAdapter(this, data, IS_HOST)
            GameAPI.showWinnerLoser(IS_HOST, this, data.state, timer)
        }
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
        val JSON_RESPONSE = "json_response"
    }

}