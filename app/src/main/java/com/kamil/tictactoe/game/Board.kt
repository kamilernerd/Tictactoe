package com.kamil.tictactoe.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kamil.tictactoe.databinding.BoardBinding
import com.kamil.tictactoe.databinding.MainBinding

class Board : AppCompatActivity() {

    private lateinit var binding: BoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
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