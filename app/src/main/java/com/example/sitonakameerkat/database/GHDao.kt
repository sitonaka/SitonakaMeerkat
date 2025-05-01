package com.example.sitonakameerkat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sitonakameerkat.server.GHUsers
import kotlinx.coroutines.flow.Flow

@Dao
interface GHDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: GHUsers)

    @Delete
    suspend fun deleteUsers(users: GHUsers)

    @Query("SELECT * from git_hub_users ORDER BY login")
    fun getAllUsersFlow(): Flow<List<GHUsers>>
}
