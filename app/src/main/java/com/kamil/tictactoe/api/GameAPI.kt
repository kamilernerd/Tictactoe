package com.kamil.tictactoe.api

import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kamil.tictactoe.R
import com.kamil.tictactoe.data.StateList
import com.kamil.tictactoe.data.flattenOutState
import java.util.*

typealias CheckGameStateCallback = (state: StateList, p1winner: Boolean, p2winner: Boolean) -> Unit

enum class PLAYER {
    NONE,
    P1,
    P2
}

object GameAPI {

    fun showWinnerLoser(is_host: Boolean, parentActivity: AppCompatActivity, state: StateList, timer: Timer) {
        checkGameState(state) { _, p1winner, p2winner ->
            if (is_host) {
                if (p1winner) {
                    playEndingSequence(parentActivity, "You won!")
                } else if (p2winner) {
                    playEndingSequence(parentActivity, "You lost!")
                }
            } else {
                if (p1winner) {
                    playEndingSequence(parentActivity, "You lost!")
                } else if (p2winner) {
                    playEndingSequence(parentActivity, "You won!")
                }
            }

            timer.cancel()
        }
    }

    private fun playEndingSequence(parentActivity: AppCompatActivity, message: String) {
        val endGameTextView = parentActivity.findViewById<TextView>(R.id.endGameMessage)
        endGameTextView.text = message

        val translateAnimation = TranslateAnimation(0f, 0f, 0f, -400f)
        translateAnimation.duration = 1500

        endGameTextView.startAnimation(translateAnimation)

        endGameTextView.postOnAnimationDelayed( {
            parentActivity.finish()
        }, 1450)
    }

    private fun checkGameState(currentState: StateList, callback: CheckGameStateCallback) {
        // There is no reason to do any advanced logic here, simply check for patterns in 3x3 grid
        // We have only 8 possible outcomes from tictactoe.
        val state = flattenOutState(currentState)

        if (checkRows(state, PLAYER.P1) || checkCols(state, PLAYER.P1) || checkAcross(state, PLAYER.P1)) {
            callback(currentState, true, false)
        } else if (checkRows(state, PLAYER.P2) || checkCols(state, PLAYER.P2) || checkAcross(state, PLAYER.P2)) {
            callback(currentState, false, true)
        }
    }

    private fun checkRows(state: MutableList<Int>, player: PLAYER): Boolean {
        if ((state[0] == player.ordinal && state[1] == player.ordinal && state[2] == player.ordinal) || // Row 1
            (state[3] == player.ordinal && state[4] == player.ordinal && state[5] == player.ordinal) || // Row 2
            (state[6] == player.ordinal && state[7] == player.ordinal && state[8] == player.ordinal)    // Row 3
        ) {
            return true
        }
        return false
    }

    private fun checkCols(state: MutableList<Int>, player: PLAYER): Boolean {
        if ((state[0] == player.ordinal && state[3] == player.ordinal && state[6] == player.ordinal) || // Col 1
            (state[1] == player.ordinal && state[4] == player.ordinal && state[7] == player.ordinal) || // Col 2
            (state[2] == player.ordinal && state[5] == player.ordinal && state[8] == player.ordinal)    // Col 3
        ) {
            return true
        }
        return false
    }

    private fun checkAcross(state: MutableList<Int>, player: PLAYER): Boolean {
        if ((state[0] == player.ordinal && state[4] == player.ordinal && state[8] == player.ordinal) || // Cross from row 1 start to row 3 end
            (state[2] == player.ordinal && state[4] == player.ordinal && state[6] == player.ordinal)    // Cross from row 1 end to row 3 start)
        ) {
            return true
        }
        return false
    }
}