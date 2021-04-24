package com.kamil.tictactoe.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameState(
    val players: MutableList<String>,
    val gameId: String,
    val state: List<List<Int>>
): Parcelable