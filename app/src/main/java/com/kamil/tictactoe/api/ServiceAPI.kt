package com.kamil.tictactoe.api

import android.content.Context
import android.opengl.Visibility
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.kamil.tictactoe.R
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.data.StateList
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import com.android.volley.RequestQueue as RequestQueue1

typealias JoinGameCallback = (gameId: String, json: GameState) -> Unit
typealias CreateGameCallback = (json: GameState) -> Unit
typealias UpdateGameCallback = (json: GameState) -> Unit
typealias PollGameCallback = (json: GameState) -> Unit
typealias PullDataCallback = (data: GameState, timer: Timer) -> Unit
typealias GenericErrorCallback = (message: String) -> Unit

object ServiceAPI {

    private const val BASE_URI = "https://generic-game-service.herokuapp.com"
    private const val JOIN_GAME = "$BASE_URI/game/join"
    private const val CREATE_GAME = "$BASE_URI/game"

    private const val API_KEY = "gjF9ebA3Ix"

    /**
     * Function to rebuild json string
     * Somehow 2d state array is converted to a string of 2d array and makes gson go nuts
     * @param response [String] Server response
     * @return [String] New json string
     */
    private fun rebuildJsonString(response: String): String {
        val jsonResponse = JSONObject(response)
        val state = jsonResponse.getString("state")
        val players = jsonResponse.getString("players")
        val gameId = jsonResponse.getString("gameId")

        // Somehow 2d state array is converted to a string of 2d array and makes gson go nuts
        return "{\"players\": $players, \"gameId\": \"$gameId\", \"state\": $state}"
    }

    /**
     * Shows spinner in main activity
     */
    private fun showProgressBar(parent: AppCompatActivity) {
        val progressBar = parent.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 100
        progressBar.min = 0
        progressBar.visibility = View.VISIBLE
    }

    /**
     * Hides spinner from main activity
     */
    private fun hideProgressBar(parent: AppCompatActivity) {
        val progressBar = parent.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
    }

    /**
     * Pull game data
     *
     * @param requestQueue [RequestQueue1] Volley request queue
     * @param currentState [GameState] Current local game state
     * @param state [StateList] 3x3 matrix of local game state
     * @param callback [UpdateGameCallback] Callback when updating game data went successfully
     */
    fun updateGame(requestQueue: RequestQueue1, currentState: GameState, state: StateList, callback: UpdateGameCallback, errorCallback: GenericErrorCallback) {
        val body = JSONObject()
        body.put("gameId", currentState.gameId)
        body.put("players", currentState.players)
        body.put("state", state)

        val request = object : JsonObjectRequest(Method.POST, "${BASE_URI}/game/${currentState.gameId}/update", body, Response.Listener { response ->
                callback(Gson().fromJson(rebuildJsonString(response.toString()), GameState::class.java))
            },
            Response.ErrorListener { error ->
                errorCallback("Could not update match!")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = API_KEY
                return headers
            }
        }
        requestQueue.add(request)
    }

    /**
     * Pull game data
     *
     * @param requestQueue [RequestQueue1] Volley request queue
     * @param gameId [StateList] Game id to join
     * @param callback [PollGameCallback] Callback when pulling game data went successfully
     */
    fun pollGame(requestQueue: RequestQueue1, gameId: String, callback: PollGameCallback, errorCallback: GenericErrorCallback) {
        val request = object : JsonObjectRequest(Method.GET, "${BASE_URI}/game/$gameId/poll", null, Response.Listener { response ->
                callback(Gson().fromJson(rebuildJsonString(response.toString()), GameState::class.java))
            },
            Response.ErrorListener { error ->
                errorCallback("Could not pull game data!")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = API_KEY
                return headers
            }
        }
        requestQueue.add(request)
    }

    /**
     * Join existing game
     *
     * @param requestQueue [RequestQueue1] Volley request queue
     * @param gameId [StateList] Game id to join
     * @param playerName [String] Game creator name
     * @param callback [JoinGameCallback] Callback when user joined successfully
     */
    fun joinGame(parent: AppCompatActivity, requestQueue: RequestQueue1, gameId: String, playerName: String, callback: JoinGameCallback, errorCallback: GenericErrorCallback) {
        val body = JSONObject()
        body.put("gameId", gameId)
        body.put("player", playerName)

        showProgressBar(parent)

        val request = object : JsonObjectRequest(Method.POST, "${BASE_URI}/game/$gameId/join", body, Response.Listener { response ->
                hideProgressBar(parent)
                callback(gameId, Gson().fromJson(rebuildJsonString(response.toString()), GameState::class.java))
            },
            Response.ErrorListener { error ->
                errorCallback("Could not join the game!")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = API_KEY
                return headers
            }
        }
        requestQueue.add(request)
    }

    /**
     * Create new game
     *
     * @param requestQueue [RequestQueue1] Volley request queue
     * @param playerName [String] Game creator name
     * @param matchState [StateList] Initial game state
     * @param callback [CreateGameCallback] Callback when game has been created successfully
     */
    fun createGame(parent: AppCompatActivity, requestQueue: RequestQueue1, playerName: String, matchState: StateList, callback: CreateGameCallback, errorCallback: GenericErrorCallback) {
        val body = JSONObject()
        body.put("player", playerName)
        body.put("state", matchState)

        showProgressBar(parent)

        val request = object : JsonObjectRequest(Method.POST, CREATE_GAME, body, Response.Listener { response ->
            hideProgressBar(parent)
            callback(Gson().fromJson(rebuildJsonString(response.toString()), GameState::class.java))
        }, Response.ErrorListener { error ->
            errorCallback("Could not create new game!")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = API_KEY
                return headers
            }
        }
        requestQueue.add(request)
    }

    /**
     * Pulls data from backend every 5 seconds
     *
     * @param context [Context] Current context
     * @param gameId [String] Game id to pull from
     * @param callback [PullDataCallback] Callback function
     * @return [Timer]
     */
    fun startPolling(context: Context, gameId: String, callback: PullDataCallback, errorCallback: GenericErrorCallback): Timer {
        val handler = Handler()
        val timer = Timer()
        val doAsynchronousTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {
                        pollGame(Volley.newRequestQueue(context), gameId, {
                            callback(it, timer)
                        }, {
                            errorCallback(it)
                        })
                    } catch (e: Exception) {
                        // TODO Auto-generated catch block
                    }
                }
            }
        }
        timer.schedule(doAsynchronousTask, 0, 5000)
        return timer
    }
}