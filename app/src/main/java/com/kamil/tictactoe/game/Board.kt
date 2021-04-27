package com.kamil.tictactoe.game

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.kamil.tictactoe.R
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.databinding.FragmentGridListBinding
import com.kamil.tictactoe.grid.GridRecyclerViewAdapter
import com.kamil.tictactoe.api.GameAPI
import com.kamil.tictactoe.api.ServiceAPI
import com.kamil.tictactoe.grid.GridRecyclerViewAdapter.Companion.BLOCK_CLICK
import java.util.*


class Board : AppCompatActivity() {

    private lateinit var binding: FragmentGridListBinding

    private var data: GameState? = null
    private var isHost: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentGridListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getParcelableExtra(JSON_RESPONSE)
        isHost = intent.getBooleanExtra(IS_HOST, isHost)

        val gridLayoutManager = GridLayoutManager(this, 3)
        val gridRecyclerViewAdapter = GridRecyclerViewAdapter(this, data!!, isHost)

        binding.gameId.text = "${applicationContext.getString(R.string.game_code)} ${data!!.gameId}"

        binding.gridLayout.layoutManager = gridLayoutManager
        binding.gridLayout.adapter = gridRecyclerViewAdapter
    }

    override fun onStart() {
        super.onStart()

        ServiceAPI.startPolling(this, data!!.gameId, { data: GameState, timer: Timer ->
            binding.gridLayout.adapter = GridRecyclerViewAdapter(this, data, isHost)
            GameAPI.showWinnerLoser(isHost, this, data.state, timer)
            BLOCK_CLICK = false
        }, {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show();
        })
    }

    companion object {
        const val TAG = "BoardActivity"
        const val IS_HOST = "IS_HOST"
        const val JSON_RESPONSE = "json_response"
    }

}