package com.kamil.tictactoe.services

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.kamil.tictactoe.data.GameState
import com.kamil.tictactoe.data.StateList
import org.json.JSONObject
import com.android.volley.RequestQueue as RequestQueue1

typealias JoinGameCallback = (gameId: String, json: GameState) -> Unit
typealias CreateGameCallback = (json: GameState) -> Unit
typealias PollGameCallback = (json: GameState) -> Unit
typealias UpdateGameCallback = (json: GameState) -> Unit

object GameAPI {

    private const val BASE_URI = "https://api.kamiloracz.no"
    private const val JOIN_GAME = "$BASE_URI/game/join"
    private const val CREATE_GAME = "$BASE_URI/game"

    fun updateGame(requestQueue: RequestQueue1, currentState: GameState, callback: UpdateGameCallback) {
        val body = JSONObject()
        body.put("gameId", currentState.gameId)
        body.put("players", currentState.players)
        body.put("state", currentState.state)

        val request = object : JsonObjectRequest(Method.POST, "$BASE_URI/game/${currentState.gameId}/update", body,
            Response.Listener { response ->
                Log.println(Log.VERBOSE, "GameAPI", response.toString())

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
        /* TODO */
    }

    fun joinGame(requestQueue: RequestQueue1, gameId: String, playerName: String, callback: JoinGameCallback) {
        val body = JSONObject()
        body.put("gameId", gameId)
        body.put("player", playerName)

        val request = object : JsonObjectRequest(Method.POST, "$BASE_URI/game/$gameId/join", body,
            Response.Listener { response ->
                Log.println(Log.VERBOSE, "GameAPI", response.toString())
                callback(gameId, Gson().fromJson(response.toString(), GameState::class.java))
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

        Log.println(Log.VERBOSE, "GameAPI", CREATE_GAME)

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
}