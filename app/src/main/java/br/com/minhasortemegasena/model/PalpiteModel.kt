package br.com.minhasortemegasena.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = "palpite_table")
data class PalpiteModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val palpiteNumbers: List<Int>,
    val typeGame: String
):Parcelable
