package br.com.minhasortemegasena.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.minhasortemegasena.model.PalpiteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PalpiteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(palpiteModel: PalpiteModel)

    @Query("SELECT * FROM palpite_table")
    fun getAllPalpites(): Flow<List<PalpiteModel>>

    @Delete
    suspend fun delete(palpiteModel: PalpiteModel)

}