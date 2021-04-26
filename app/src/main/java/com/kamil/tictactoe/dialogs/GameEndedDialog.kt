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
import com.kamil.tictactoe.databinding.DialogEndedGameBinding
import com.kamil.tictactoe.databinding.DialogJoinGameBinding
import com.kamil.tictactoe.game.Board
import com.kamil.tictactoe.services.GameAPI
import org.json.JSONObject

class GameEndedDialog(
    val message: String
): DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater = requireActivity().layoutInflater
        val binding = DialogEndedGameBinding.inflate(inflater)

        builder.setTitle("Game ended")
        binding.messageView.text = message

        builder.setPositiveButton("Play again") { dialog, which ->
            dismiss()
        }

        builder.setView(binding.root)
        builder.create()

        return builder.show()
    }

    companion object {
        const val TAG = "JoinGameDialog"
        val JSON_RESPONSE = "json_response"
    }

}
