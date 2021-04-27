package com.kamil.tictactoe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.android.volley.toolbox.Volley
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.data.initialState
import com.kamil.tictactoe.databinding.DialogCreateGameBinding
import com.kamil.tictactoe.game.Board
import com.kamil.tictactoe.api.ServiceAPI

class CreateGameDialog(
    val parent: AppCompatActivity
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater = requireActivity().layoutInflater
        val binding = DialogCreateGameBinding.inflate(inflater)

        builder.setTitle("Create game")
        builder.setPositiveButton("Create") { _, _ ->
            val username = binding.username.text.toString()
            if (username.isNotEmpty()) {
                ServiceAPI.createGame(parent, Volley.newRequestQueue(context), username, initialState, { json: GameState ->
                    val intent = Intent(builder.context, Board::class.java).apply {
                        val bundle = Bundle()
                        bundle.putParcelable(JSON_RESPONSE, json)
                        putExtras(bundle)
                        putExtra(IS_HOST, true)
                    }
                    builder.context.startActivity(intent)
                }, {
                    Toast.makeText(builder.context, it, Toast.LENGTH_SHORT).show();
                })
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.setView(binding.root)
        builder.create()

        return builder.show()
    }

    companion object {
        const val TAG = "CreateGameDialog"
        const val IS_HOST = "IS_HOST"
        const val JSON_RESPONSE = "json_response"
    }

}