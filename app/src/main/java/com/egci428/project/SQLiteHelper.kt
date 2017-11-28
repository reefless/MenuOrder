package com.egci428.project

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by Mosswrat on 11/14/2017 AD.
 */
class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w(SQLiteHelper::class.java!!.name,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data")
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU)
        onCreate(db)
    }

    companion object {

        val TABLE_MENU = "menu"
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "foodName"
        val COLUMN_PRICE = "price"
        val COLUMN_IMG = "img"
        private val DATABASE_NAME = "menu.db"
        private val DATABASE_VERSION = 1

        // Database creation sql statement
        private val DATABASE_CREATE = ("create table "
                + TABLE_MENU + "(" + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_NAME
                + " text not null," + COLUMN_PRICE + " text not null," + COLUMN_IMG + " BLOB NOT NULL);")
    }
}