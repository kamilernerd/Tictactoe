package com.kamil.tictactoe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.android.volley.toolbox.Volley
import com.kamil.tictactoe.Main
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.databinding.DialogJoinGameBinding
import com.kamil.tictactoe.game.Board
import com.kamil.tictactoe.services.GameAPI
import org.json.JSONObject

class JoinGameDialog: DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater = requireActivity().layoutInflater
        val binding = DialogJoinGameBinding.inflate(inflater)

        builder.setTitle("Join game")

        builder.setPositiveButton("Join") { dialog, which ->
            val username = binding.username.text.toString()
            val gameCode = binding.gameCode.text.toString()

            if (username.isNotEmpty() && gameCode.isNotEmpty()) {
                GameAPI.joinGame(Volley.newRequestQueue(builder.context), gameCode, username) { gameId: String, json: GameState ->
                    val intent = Intent(builder.context, Board::class.java).apply {
                        val bundle = Bundle()
                        bundle.putParcelable(JSON_RESPONSE, json)
                        putExtras(bundle)
                    }
                    builder.context.startActivity(intent)
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        builder.setView(binding.root)
        builder.create()

        return builder.show()
    }

    companion object {
        val JSON_RESPONSE = "json_response"
    }

}
