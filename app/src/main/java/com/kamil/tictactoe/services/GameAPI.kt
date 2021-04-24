package com.kamil.tictactoe.services

object GameAPI {

<<<<<<< Updated upstream
    private val baseURI = "https://api.kamiloracz.no/
=======
    private const val BASE_URI = "https://api.kamiloracz.no"
    private const val JOIN_GAME = "$BASE_URI/game/join"
    private const val CREATE_GAME = "$BASE_URI/game"

    fun joinGame(requestQueue: RequestQueue, gameId: String, playerName: String) {
        val body = JSONObject()
        body.put("gameId", gameId)
        body.put("player", playerName)
>>>>>>> Stashed changes


<<<<<<< Updated upstream

}
=======
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

    fun createGame(requestQueue: RequestQueue, playerName: String, matchState: List<List<Int>>) {
        val body = JSONObject()

        body.put("player", playerName)
        body.put("state", matchState)

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
>>>>>>> Stashed changes
