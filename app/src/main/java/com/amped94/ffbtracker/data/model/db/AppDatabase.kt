package com.amped94.ffbtracker.data.model.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amped94.ffbtracker.MainApplication
import com.amped94.ffbtracker.data.api.model.SleeperPlayer
import com.amped94.ffbtracker.data.model.db.dao.LeagueDao
import com.amped94.ffbtracker.data.model.db.dao.PlayerDao
import com.amped94.ffbtracker.data.model.db.dao.PlayerLeagueCrossRefDao
import com.amped94.ffbtracker.data.model.db.dao.UserDao
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.model.db.entity.PlayerLeagueCrossRef
import com.amped94.ffbtracker.data.model.db.entity.User

@Database(
    version = 1,
    entities = [
        User::class,
        League::class,
        Player::class,
        PlayerLeagueCrossRef::class
    ],
)
@TypeConverters(LeagueConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun leagueDao(): LeagueDao
    abstract fun playerDao(): PlayerDao
    abstract fun playerLeagueCrossRefDao(): PlayerLeagueCrossRefDao

    companion object {
        val instance: AppDatabase = Room.databaseBuilder(
            MainApplication.getContext(),
            AppDatabase::class.java, "ffbtracker-db"
        ).fallbackToDestructiveMigration().build()
    }
}