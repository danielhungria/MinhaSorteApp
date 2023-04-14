package br.com.minhasortemegasena.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.minhasortemegasena.converters.Converters
import br.com.minhasortemegasena.model.PalpiteModel

@Database(
    entities = [PalpiteModel::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class PalpiteDatabase: RoomDatabase() {

    abstract fun getPalpiteDao(): PalpiteDao

}