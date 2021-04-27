package com.kamil.tictactoe.api

import android.util.Log
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

    /**
     * Based on who's the game host display message to both users
     * Message tells users if the won or lost the current game
     *
     * @param is_host [Boolean] Flag to check if user has created current game
     * @param parentActivity [AppCompatActivity] Parent activity for game (Currently [Board]]
     * @param state [StateList] Current game state
     * @param timer [Timer] Timer reference to stop pulling from server
     * @return [Unit]
     */
    fun showWinnerLoser(is_host: Boolean, parentActivity: AppCompatActivity, state: StateList, timer: Timer): Unit {
        checkGameState(state) { _, p1winner, p2winner ->
            if (is_host) {
                if (p1winner) {
                    playEndingSequence(parentActivity, "You won!")
                } else if (p2winner) {
                    playEndingSequence(parentActivity, "You lost!")
                } else if (p1winner && p2winner) {
                    playEndingSequence(parentActivity, "Draw!")
                }
            } else {
                if (p1winner) {
                    playEndingSequence(parentActivity, "You lost!")
                } else if (p2winner) {
                    playEndingSequence(parentActivity, "You won!")
                } else if (p1winner && p2winner) {
                    playEndingSequence(parentActivity, "Draw!")
                }
            }

            timer.cancel()
        }
    }

    /**
     * Show if user has won or lost the game
     * Animation slides up from the bottom then kills the parent activity
     *
     * @param parentActivity [AppCompatActivity] Parent activity for game (Currently [Board])
     * @param message [String] Message to show in the animation
     * @return [Unit]
     */
    private fun playEndingSequence(parentActivity: AppCompatActivity, message: String): Unit {
        val endGameTextView = parentActivity.findViewById<TextView>(R.id.endGameMessage)
        endGameTextView.text = message

        val translateAnimation = TranslateAnimation(0f, 0f, 0f, -400f)
        translateAnimation.duration = 1500

        endGameTextView.startAnimation(translateAnimation)

        endGameTextView.postOnAnimationDelayed( {
            parentActivity.finish()
        }, 1450)
    }

    /**
     * Check if player 1 or player 2 has won or lost the gmae
     *
     * @param currentState [StateList] 3x3 matrix of current game state
     * @param callback [CheckGameStateCallback] Callback function
     * @return [Unit]
     */
    private fun checkGameState(currentState: StateList, callback: CheckGameStateCallback): Unit {
        // There is no reason to do any advanced logic here, simply check for patterns in 3x3 grid
        // We have only 8 possible outcomes from tictactoe.
        val state = flattenOutState(currentState)

        if (checkRows(state, PLAYER.P1) || checkCols(state, PLAYER.P1) || checkAcross(state, PLAYER.P1)) {
            callback(currentState, true, false)
        } else if (checkRows(state, PLAYER.P2) || checkCols(state, PLAYER.P2) || checkAcross(state, PLAYER.P2)) {
            callback(currentState, false, true)
        }

        // Handle draw
        if (!state.contains(0)) {
            callback(currentState, false, false)
        }
    }

    /**
     * Check if player has checked 3 fields in a row in one row in any of the rows
     *
     * @param state [MutableList] Flatten out state
     * @param player [PLAYER] Enum to determine what player stands in field
     * @return [Boolean]
     */
    private fun checkRows(state: MutableList<Int>, player: PLAYER): Boolean {
        if ((state[0] == player.ordinal && state[1] == player.ordinal && state[2] == player.ordinal) || // Row 1
            (state[3] == player.ordinal && state[4] == player.ordinal && state[5] == player.ordinal) || // Row 2
            (state[6] == player.ordinal && state[7] == player.ordinal && state[8] == player.ordinal)    // Row 3
        ) {
            return true
        }
        return false
    }

    /**
     * Check if player has checked 3 fields in a row in one column in any of the columns
     *
     * @param state [MutableList] Flatten out state
     * @param player [PLAYER] Enum to determine what player stands in field
     * @return [Boolean]
     */
    private fun checkCols(state: MutableList<Int>, player: PLAYER): Boolean {
        if ((state[0] == player.ordinal && state[3] == player.ordinal && state[6] == player.ordinal) || // Col 1
            (state[1] == player.ordinal && state[4] == player.ordinal && state[7] == player.ordinal) || // Col 2
            (state[2] == player.ordinal && state[5] == player.ordinal && state[8] == player.ordinal)    // Col 3
        ) {
            return true
        }
        return false
    }

    /**
     * Check if player has checked in 3 fields in a cross pattern
     *
     * 0 0 P
     *
     * 0 P 0
     *
     * P 0 0
     *
     * Or
     *
     * P 0 0
     *
     * 0 P 0
     *
     * 0 0 P
     *
     * @param state [MutableList] Flatten out state
     * @param player [PLAYER] Enum to determine what player stands in field
     * @return [Boolean]
     */
    private fun checkAcross(state: MutableList<Int>, player: PLAYER): Boolean {
        if ((state[0] == player.ordinal && state[4] == player.ordinal && state[8] == player.ordinal) || // Cross from row 1 start to row 3 end
            (state[2] == player.ordinal && state[4] == player.ordinal && state[6] == player.ordinal)    // Cross from row 1 end to row 3 start)
        ) {
            return true
        }
        return false
    }
}