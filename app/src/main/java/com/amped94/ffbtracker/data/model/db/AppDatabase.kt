package com.amped94.ffbtracker.data.model.db

import androidx.room.*
import com.amped94.ffbtracker.MainApplication
import com.amped94.ffbtracker.data.model.db.dao.LeagueDao
import com.amped94.ffbtracker.data.model.db.dao.PlayerDao
import com.amped94.ffbtracker.data.model.db.dao.UserDao
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.model.db.entity.User

@Database(
    version = 2,
    entities = [User::class, League::class, Player::class],
)
@TypeConverters(LeagueConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun leagueDao(): LeagueDao
    abstract fun playerDao(): PlayerDao

    companion object {
        val instance: AppDatabase = Room.databaseBuilder(
            MainApplication.getContext(),
            AppDatabase::class.java, "ffbtracker-db"
        ).fallbackToDestructiveMigration().build()
    }
}