package com.ke.hs.module.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ke.hs.module.db.entity.Game
import com.ke.hs.module.entity.Card


const val DATABASE_VERSION = 1

@Database(
    entities = [Card::class, Game::class, ZonePositionChangedEvent::class],
    version = DATABASE_VERSION,
    exportSchema = true,
)
@TypeConverters(
    CardClassesConvert::class, MechanicsListConvert::class
)
abstract class Database : RoomDatabase() {


    abstract fun cardDao(): CardDao

    abstract fun gameDao(): GameDao


    abstract fun zonePositionChangedEventDao(): ZonePositionChangedEventDao

    companion object {
        const val VERSION = 3
    }
}

