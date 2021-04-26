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

/**
 * Build state list from [List]
 */
fun buildStateList(state: List<Int>): StateList {
    val result = mutableListOf<List<Int>>()
    val tmpList = mutableListOf<Int>()
    var count: Int = 0
    for (element in state) {
        if (count % 3 == 0) {
            result.add(tmpList)
            tmpList.clear()
        }
        tmpList.add(element)
        count++
    }
    return result
}

public val initialState: StateList = listOf(
    listOf(0, 0, 0),
    listOf(0, 0, 0),
    listOf(0, 0, 0)
)