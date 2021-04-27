package com.kamil.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kamil.tictactoe.databinding.MainBinding
import com.kamil.tictactoe.dialogs.CreateGameDialog
import com.kamil.tictactoe.dialogs.JoinGameDialog

class Main : AppCompatActivity() {

    private lateinit var binding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
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
}