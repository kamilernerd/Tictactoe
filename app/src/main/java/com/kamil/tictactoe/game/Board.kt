package com.kamil.tictactoe.game

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.databinding.BoardBinding
import com.kamil.tictactoe.dialogs.JoinGameDialog.Companion.JSON_RESPONSE

class Board : AppCompatActivity() {

    private lateinit var binding: BoardBinding

    private var data: GameState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getParcelableExtra(JSON_RESPONSE)
    }

    override fun onStart() {
        super.onStart()

        Log.println(Log.WARN, TAG, data?.players.toString())

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