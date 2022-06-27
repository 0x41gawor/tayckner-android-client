package pl.gawor.android.tayckner.day_tracker.repository.dbhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "tayckner_desktop_db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createCategoryTable =
            """
                CREATE TABLE category (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT  NOT NULL,
                description TEXT default null,
                color TEXT  NOT NULL,
                user_id INTEGER  NOT NULL
                );
            """.trimIndent()
        db?.execSQL(createCategoryTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS category")
        onCreate(db)
    }


}