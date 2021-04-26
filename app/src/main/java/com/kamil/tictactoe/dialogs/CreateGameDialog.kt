package com.kamil.tictactoe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.android.volley.toolbox.Volley
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.data.initialState
import com.kamil.tictactoe.databinding.DialogCreateGameBinding
import com.kamil.tictactoe.game.Board
import com.kamil.tictactoe.services.GameAPI

class CreateGameDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DialogCreateGameBinding.inflate(inflater)

            builder.apply {
                setTitle("Create game")
                setPositiveButton("Create") { dialog, which ->

                    val username = binding.username.text.toString()

                    if (username.isNotEmpty()) {
                        GameAPI.createGame(Volley.newRequestQueue(context), username, initialState) { json: GameState ->
                            val intent = Intent(builder.context, Board::class.java).apply {
                                val bundle = Bundle()
                                bundle.putParcelable(JSON_RESPONSE, json)
                                putExtras(bundle)
                            }
                            builder.context.startActivity(intent)
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

    companion object {
        const val TAG = "CreateGameDialog"
        val JSON_RESPONSE = "json_response"
    }

}