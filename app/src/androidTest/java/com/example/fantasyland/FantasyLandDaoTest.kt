package com.example.fantasyland

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fantasyland.data.FantasyLandDao
import com.example.fantasyland.data.FantasyLandDatabase
import com.example.fantasyland.data.Game
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FantasyLandDaoTest {

    private lateinit var db: FantasyLandDatabase
    private lateinit var dao: FantasyLandDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, FantasyLandDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.fantasyLandDao
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetGame() = runBlocking {
        val game = Game()
        dao.insert(game)
        assertEquals(1, dao.count())

        val loadedGame = dao.get(1)                     // autoincrement primary key starts from 1
        assertEquals("Guest", loadedGame?.nickName)

        dao.clear()
        assertEquals(0, dao.count())
    }
}