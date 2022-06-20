package com.example.fantasyland.data

import javax.inject.Inject

class GamesRepository @Inject constructor(
    private val fantasyLandDao: FantasyLandDao
){
    fun numberOfItemsInDB() = fantasyLandDao.count()
}