package com.kamil.tictactoe.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

typealias StateList = List<List<Int>>

@Parcelize
data class GameState(
    val players: MutableList<String>,
    val gameId: String,
    var state: StateList
): Parcelable

/**
 * Flatten out the [StateList]
 */
fun flattenOutState(state: StateList): MutableList<Int> {
    val result = ArrayList<Int>()
    for (element in state) {
        result.addAll(element)
    }
    return result
}

/**
 * Build state list from [List]
 */
fun buildStateList(state: List<Int>): StateList {
    return state.chunked(3)
}

val initialState: StateList = listOf(
    listOf(0, 0, 0),
    listOf(0, 0, 0),
    listOf(0, 0, 0)
)