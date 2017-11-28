package com.egci428.project

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import com.egci428.project.model.menu
import com.egci428.project.model.menuTemplate
import java.sql.SQLException
import android.graphics.BitmapFactory
import android.graphics.Bitmap



/**
 * Created by Mosswrat on 11/14/2017 AD.
 */
class DataSource(context: Context) {
    private var database: SQLiteDatabase? = null
    private val dbHelper: SQLiteHelper
    private val allColumns = arrayOf<String>(SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_NAME, SQLiteHelper.COLUMN_PRICE)

    // make sure to close the cursor
    val allComments: List<menu>
        get() {
            val comments = ArrayList<menu>()

            val cursor = database!!.query(SQLiteHelper.TABLE_MENU,
                    allColumns, null, null, null, null, null)

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val comment = cursorToComment(cursor)
                comments.add(comment)
                cursor.moveToNext()
            }
            cursor.close()
            return comments
        }

    init {
        dbHelper = SQLiteHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dbHelper.getWritableDatabase()
    }

    fun close() {
        dbHelper.close()
    }

    fun createComment(comment: String, price: String, img: ByteArray): menu {
        val values = ContentValues()
        values.put(SQLiteHelper.COLUMN_NAME, comment)
        values.put(SQLiteHelper.COLUMN_PRICE, price)
        values.put(SQLiteHelper.COLUMN_IMG, img)
        val insertId = database!!.insert(SQLiteHelper.TABLE_MENU, null,
                values)
        val cursor = database!!.query(SQLiteHelper.TABLE_MENU,
                allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null)
        cursor.moveToFirst()
        val newComment = cursorToComment(cursor)
        cursor.close()
        return newComment
    }
    fun retrieveMenu():menuTemplate {
        val selectQuery = "SELECT * FROM menu"
        val cursor = database!!.rawQuery(selectQuery, null)
        val count = cursor.getCount()
        var x = 0
        val data: menuTemplate = menuTemplate()
        cursor.moveToFirst()
        while(x< count) {
            Log.d("Cursor", "ID: ${cursor.getLong(0)} FoodName: ${cursor.getString(1)} Price: ${cursor.getString(2)}")
            val byte: ByteArray = cursor.getBlob(3)

            data.addMenu(cursor.getLong(0), cursor.getString(1), cursor.getString(2), byte )
//        data!!.foodName = cursor.getString(1)
//        data!!.price = cursor.getString(2)
            cursor.moveToNext()
            x++
        }
        cursor!!.close()
        return data
    }
    fun deleteComment(comment: menu) {
        val id = comment.id
        println("Comment deleted with id: " + id)
        database!!.delete(SQLiteHelper.TABLE_MENU, SQLiteHelper.COLUMN_ID
                + " = " + id, null)
    }

    private fun cursorToComment(cursor: Cursor): menu {
        val comment = menu()
        comment.id = cursor.getLong(0)
        comment.foodName = cursor.getString(1)
        comment.price = cursor.getString(2)
        return comment
    }
}