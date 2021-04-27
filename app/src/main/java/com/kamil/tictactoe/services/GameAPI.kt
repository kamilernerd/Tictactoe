package com.kamil.tictactoe.services

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.kamil.tictactoe.R
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.data.StateList
import com.kamil.tictactoe.data.buildStateList
import com.kamil.tictactoe.data.flattenOutState
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import com.android.volley.RequestQueue as RequestQueue1

typealias JoinGameCallback = (gameId: String, json: GameState) -> Unit
typealias CreateGameCallback = (json: GameState) -> Unit
typealias PollGameCallback = (json: GameState) -> Unit
typealias UpdateGameCallback = (json: GameState) -> Unit
typealias CheckGameStateCallback = (state: StateList, p1winner: Boolean, p2winner: Boolean) -> Unit

enum class PLAYER {
    NONE,
    P1,
    P2
}

object GameAPI {

    private const val BASE_URI = "https://api.kamiloracz.no"
    private const val JOIN_GAME = "$BASE_URI/game/join"
    private const val CREATE_GAME = "$BASE_URI/game"

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

    fun updateGame(requestQueue: RequestQueue1, currentState: GameState, state: StateList, callback: UpdateGameCallback) {
        val body = JSONObject()
        body.put("gameId", currentState.gameId)
        body.put("players", currentState.players)
        body.put("state", state)

        val request = object : JsonObjectRequest(Method.POST, "$BASE_URI/game/${currentState.gameId}/update", body,
            Response.Listener { response ->
                Log.println(Log.VERBOSE, "GameAPI UPDATE", response.toString())

                val jsonResponse = JSONObject("$response")
                val state = jsonResponse.getString("state")
                val players = jsonResponse.getString("players")
                val gameId = jsonResponse.getString("gameId")

                // Somehow 2d state array is converted to a string of 2d array and makes gson go nuts
                val customJsonStringHack = "{\"players\": $players, \"gameId\": \"$gameId\", \"state\": $state}"

                callback(Gson().fromJson(customJsonStringHack, GameState::class.java))
            },
            Response.ErrorListener { error ->
                Log.println(Log.VERBOSE, "GameAPI response error", error.toString())
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = "TestKey"
                return headers
            }
        }
        requestQueue.add(request)
    }

    fun pollGame(requestQueue: RequestQueue1, gameId: String, callback: PollGameCallback) {
        val request = object : JsonObjectRequest(Method.GET, "$BASE_URI/game/$gameId/poll", null,
            Response.Listener { response ->
                Log.println(Log.VERBOSE, "GameAPI POLL", response.toString())

                val jsonResponse = JSONObject("$response")
                val state = jsonResponse.getString("state")
                val players = jsonResponse.getString("players")
                val gameId = jsonResponse.getString("gameId")

                // Somehow 2d state array is converted to a string of 2d array and makes gson go nuts
                val customJsonStringHack = "{\"players\": $players, \"gameId\": \"$gameId\", \"state\": $state}"
                val decodedJson = Gson().fromJson(customJsonStringHack, GameState::class.java)
                callback(decodedJson)
            },
            Response.ErrorListener { error ->
                Log.println(Log.VERBOSE, "GameAPI response error", error.toString())
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = "TestKey"
                return headers
            }
        }
        requestQueue.add(request)
    }

    fun joinGame(requestQueue: RequestQueue1, gameId: String, playerName: String, callback: JoinGameCallback) {
        val body = JSONObject()
        body.put("gameId", gameId)
        body.put("player", playerName)

        val request = object : JsonObjectRequest(Method.POST, "$BASE_URI/game/$gameId/join", body,
            Response.Listener { response ->
                Log.println(Log.VERBOSE, "GameAPI JOIN", response.toString())

                val jsonResponse = JSONObject("$response")
                val state = jsonResponse.getString("state")
                val players = jsonResponse.getString("players")
                val gameId = jsonResponse.getString("gameId")

                // Somehow 2d state array is converted to a string of 2d array and makes gson go nuts
                val customJsonStringHack = "{\"players\": $players, \"gameId\": \"$gameId\", \"state\": $state}"

                callback(gameId, Gson().fromJson(customJsonStringHack, GameState::class.java))
            },
            Response.ErrorListener { error ->
                Log.println(Log.VERBOSE, "GameAPI response error", error.toString())
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = "TestKey"
                return headers
            }
        }
        requestQueue.add(request)
    }

    fun createGame(requestQueue: RequestQueue1, playerName: String, matchState: StateList, callback: CreateGameCallback) {
        val body = JSONObject()
        body.put("player", playerName)
        body.put("state", matchState)

        Log.println(Log.VERBOSE, "GameAPI CREATE", CREATE_GAME)

        val request = object : JsonObjectRequest(Request.Method.POST, CREATE_GAME, body, Response.Listener { response ->
            val jsonResponse = JSONObject("$response")
            val state = jsonResponse.getString("state")
            val players = jsonResponse.getString("players")
            val gameId = jsonResponse.getString("gameId")

            // Somehow 2d state array is converted to a string of 2d array and makes gson go nuts
            val customJsonStringHack = "{\"players\": $players, \"gameId\": \"$gameId\", \"state\": $state}"

            callback(Gson().fromJson(customJsonStringHack, GameState::class.java))
        }, Response.ErrorListener { error ->
            Log.println(Log.VERBOSE, "GameAPI response error", error.toString())
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = "TestKey"
                return headers
            }
        }
        requestQueue.add(request)
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

    fun pollDataTimer(context: Context, gameId: String, callback: (data: GameState, timer: Timer) -> Unit): Timer {
        val handler = Handler()
        val timer = Timer()
        val doAsynchronousTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post(Runnable {
                    try {
                        pollGame(Volley.newRequestQueue(context), gameId) {
                            callback(it, timer)
                        }
                    } catch (e: Exception) {
                        // TODO Auto-generated catch block
                    }
                })
            }
        }
        timer.schedule(doAsynchronousTask, 0, 5000) //execute in every 50000 ms
        return timer
    }

}