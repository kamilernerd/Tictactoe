package com.kamil.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kamil.tictactoe.databinding.MainBinding
import com.kamil.tictactoe.dialogs.JoinGameDialog

class Main : AppCompatActivity() {

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
            // Open modal for creating new game
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