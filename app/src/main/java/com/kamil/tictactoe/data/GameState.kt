package com.kamil.tictactoe.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

typealias StateList = List<List<Int>>

@Parcelize
data class GameState(
    val players: MutableList<String>,
    val gameId: String,
    val state: StateList
): Parcelable

/**
 * Flatten out the [StateList]
 */
fun flattenOutState(state: StateList): List<Int> {
    val result = ArrayList<Int>()
    for (element in state) {
        result.addAll(element)
    }
    return result
}

public val initialState: StateList = listOf(
    listOf(0, 0, 0),
    listOf(0, 0, 0),
    listOf(0, 0, 0)
)