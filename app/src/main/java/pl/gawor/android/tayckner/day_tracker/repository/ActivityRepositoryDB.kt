package pl.gawor.android.tayckner.day_tracker.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import pl.gawor.android.tayckner.day_tracker.model.Activity
import pl.gawor.android.tayckner.day_tracker.model.Category
import pl.gawor.android.tayckner.day_tracker.repository.dbhelper.SQLiteHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ActivityRepositoryDB() {

    var dbHelper: SQLiteHelper? = null

    var context: Context? = null

    var categoryRepository: CategoryRepositoryDB? = null

    constructor(context: Context) : this() {
        dbHelper = SQLiteHelper(context)
        this.context = context
        categoryRepository = CategoryRepositoryDB(context)
    }

    fun create(activity: Activity) {
        val db = dbHelper!!.writableDatabase
        val values = ContentValues().apply {
            put("name", activity.name)
            put("start_time", activity.startTime)
            put("end_time", activity.endTime)
            put("date", activity.date.toString())
            put("duration", activity.duration)
            put("breaks",activity.breaks)
            put("category_id", activity.category.id)
        }

        val newRowId = db?.insert("activity", null, values)
    }

    fun update(activity: Activity, id: Int) {
        val db = dbHelper!!.writableDatabase
        val values = ContentValues().apply {
            put("name", activity.name)
            put("start_time", activity.startTime)
            put("end_time", activity.endTime)
            put("date", activity.date.toString())
            put("duration", activity.duration)
            put("breaks",activity.breaks)
            put("category_id", activity.category.id)
        }

        // Define 'where' part of query.
        val selection = "id LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id.toString())

        val count = db.update(
            "activity",
            values,
            selection,
            selectionArgs)
    }

    fun delete(id: Int) {
        val db = dbHelper!!.writableDatabase

        // Define 'where' part of query.
        val selection = "id LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id.toString())
        // Issue SQL statement.
        val deletedRows = db.delete("activity", selection, selectionArgs)
    }

    fun list() : List<Activity> {
        val db = dbHelper!!.readableDatabase
        val cursor = db.query(
            false,
            "activity",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val list = mutableListOf<Activity>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(0)
                val name = getString(1)
                val startTime = getString(2)
                val endTime = getString(3)
                Log.i("TAYCKNER", "Data: ${getString(4)}")
                val date = LocalDate.parse(getString(4), DateTimeFormatter.ISO_DATE)
                val duration = getInt(5)
                val breaks = getInt(6)
                val category = categoryRepository!!.read(getInt(7))
                val activity = Activity(
                    id, name, startTime, endTime, date, duration, breaks, category!!
                )
                list.add(activity)
            }
        }
        cursor.close()
        return list

    }


}