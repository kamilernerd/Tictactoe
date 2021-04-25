package com.kamil.tictactoe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.android.volley.toolbox.Volley
import com.kamil.tictactoe.databinding.DialogJoinGameBinding
import com.kamil.tictactoe.services.GameAPI

class JoinGameDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DialogJoinGameBinding.inflate(inflater)

            builder.apply {
                setTitle("Join game")

                setPositiveButton("Join") { dialog, which ->
                    val username = binding.username.text.toString()
                    val gameCode = binding.gameCode.text.toString()

                    if (username.isNotEmpty() && gameCode.isNotEmpty()) {
                        GameAPI.joinGame(Volley.newRequestQueue(context), gameCode, username) { gameId: String, json: String ->
                            Log.println(Log.INFO, "JOIN GAME DIALOG", json)
                            val intent = Intent()
                        }
                    }
                }
                setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                setView(binding.root)
            }

            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

}