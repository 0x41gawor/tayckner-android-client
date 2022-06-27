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
        val createActivityTable =
            """
                CREATE TABLE activity (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                start_time TEXT NOT NULL,
                end_time TEXT NOT NULL,
                date TEXT NOT NULL,
                duration INTEGER,
                breaks INTEGER,
                category_id INTEGER NOT NULL,
                CONSTRAINT FK_category
                    FOREIGN KEY (category_id)
                    REFERENCES category(id)
                );
            """.trimIndent()
        db?.execSQL(createCategoryTable)
        db?.execSQL(createActivityTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS category")
        db?.execSQL("DROP TABLE IF EXISTS activity")
        onCreate(db)
    }


}