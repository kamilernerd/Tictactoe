package com.kamil.tictactoe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.android.volley.RequestQueue
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
                    if(binding.username.text.toString() != "" && binding.gameCode.text.toString() != ""){
                        GameAPI.joinGame(Volley.newRequestQueue(context), "0r9et", "test1")
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