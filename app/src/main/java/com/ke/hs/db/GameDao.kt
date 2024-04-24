package com.ke.hs.db

import androidx.room.*
import com.ke.hs.db.entity.Game
import com.ke.hs.entity.CardClass
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)


    /**
     * 插入所有
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(games: List<Game>)

    @Update
    suspend fun update(game: Game)

    @Delete
    suspend fun delete(game: Game)

    /**
     * 删除全部
     */
    @Query("delete from game")
    suspend fun deleteAll()

    /**
     * 查询所有
     */
    @Query("select * from game order by end_time desc")
    fun getAll(): Flow<List<Game>>

    /**
     * 查找用户某个英雄的总对局
     */
    @Query("select * from game where user_hero = :cardClass")
    suspend fun getByHero(cardClass: CardClass): List<Game>

    /**
     * 获取总的对局数
     */
    @Query("select count(*) from game")
    suspend fun getGameCount(): Int

    /**
     * 获取玩家胜率对局数
     */
    @Query("select count(*) from game where is_user_win = 1")
    suspend fun getUserWinCount(): Int

    @Query("select * from game where user_deck_name = :name and user_deck_code = :code")
    fun getGameListByNameAndCode(name: String, code: String): Flow<List<Game>>

    /**
     * 根据卡组代码和名称查询
     */
    @Query("select * from game where user_deck_code = :code and user_deck_name = :name")
    suspend fun getByDeckCodeAndName(
        code: String,
        name: String
    ): List<Game>
}