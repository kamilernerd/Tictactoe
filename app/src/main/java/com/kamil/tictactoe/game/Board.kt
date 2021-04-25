package com.kamil.tictactoe.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.databinding.MainBinding
import com.kamil.tictactoe.dialogs.CreateGameDialog
import com.kamil.tictactoe.dialogs.JoinGameDialog

class Board : AppCompatActivity() {

    private lateinit var binding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinGameButton.setOnClickListener {
            JoinGameDialog().show(
                supportFragmentManager,
                "Join game dialog"
            )
        }

        binding.createGameButton.setOnClickListener {
            CreateGameDialog().show(
                supportFragmentManager,
                "Create game dialog"
            )
        }

    }

    override fun onResume() {
        super.onResume()
        // Check if player is in game and rejoin the game
    }

    override fun onStop() {
        super.onStop()
        // Check if player is in game and forfeit the game
    }

}