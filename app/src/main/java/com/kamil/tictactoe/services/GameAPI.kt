package com.kamil.tictactoe.services

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.kamil.tictactoe.data.GameState
import org.json.JSONObject
import java.util.*

object GameAPI {

    private const val BASE_URI = "https://api.kamiloracz.no"
    private const val JOIN_GAME = "$BASE_URI/game/join"
    private const val CREATE_GAME = "$BASE_URI/game"

    fun joinGame(requestQueue: RequestQueue, gameId: String, playerName: String) {
        val body = JSONObject()
        body.put("gameId", gameId)
        body.put("player", playerName)

        Log.println(Log.VERBOSE, "GameAPI", JOIN_GAME)

        val request = object : JsonObjectRequest(Request.Method.POST, "$BASE_URI/game/$gameId/join", body, Response.Listener { response ->
            Log.println(Log.VERBOSE, "GameAPI response", response.toString())
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

    fun createGame(requestQueue: RequestQueue, playerName: String, gameState: GameState) {
        val body = JSONObject()

        body.put("player", playerName)
        body.put("state", gameState)

        Log.println(Log.VERBOSE, "GameAPI", JOIN_GAME)

        val request = object : JsonObjectRequest(Request.Method.POST, CREATE_GAME, body, Response.Listener { response ->
            Log.println(Log.VERBOSE, "GameAPI response", response.toString())
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

private operator fun <K, V> HashMap<K, V>.set(v: V, value: V) {}
