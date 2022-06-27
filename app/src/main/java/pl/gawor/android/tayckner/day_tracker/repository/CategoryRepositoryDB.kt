package pl.gawor.android.tayckner.day_tracker.repository

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import pl.gawor.android.tayckner.day_tracker.model.Category
import pl.gawor.android.tayckner.day_tracker.repository.dbhelper.SQLiteHelper
import kotlin.coroutines.coroutineContext

class CategoryRepositoryDB() {


    var dbHelper: SQLiteHelper? = null

    var context: Context? = null

    constructor(context: Context) : this() {
        dbHelper = SQLiteHelper(context)
        this.context = context
    }

    fun list(): List<Category> {
        val db = dbHelper!!.readableDatabase
        val cursor = db.query(
            false,
            "category",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val list = mutableListOf<Category>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(0)
                val name = getString(1)
                val description = getString(2)
                val color = getString(3)
                val category = Category(id, name, description, color, null)
                list.add(category)
            }
        }
        cursor.close()
        return list
    }

    fun create(category: Category): Category? {
        val db = dbHelper!!.writableDatabase
        val values = ContentValues().apply {
            put("name", category.name)
            put("description", category.description)
            put("color", category.color)
            put("user_id", 1)
        }

        val newRowId = db?.insert("category", null, values)
        return Category(0,"","", "#ffffff",  null)
    }

    fun delete(id: Int) {
        val db = dbHelper!!.writableDatabase

        // Define 'where' part of query.
        val selection = "id LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id.toString())
        // Issue SQL statement.
        val deletedRows = db.delete("category", selection, selectionArgs)
    }

}