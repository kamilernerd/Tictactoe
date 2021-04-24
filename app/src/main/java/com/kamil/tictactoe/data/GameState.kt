package com.kamil.tictactoe.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameState(
    val players: MutableList<String>,
    val gameId: String,
    val state: List<List<Int>>
): Parcelable

public val initialState: List<List<Int>> = listOf(
    listOf(0, 0, 0),
    listOf(0, 0, 0),
    listOf(0, 0, 0)
)