package pl.gawor.android.tayckner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import pl.gawor.android.tayckner.day_tracker.model.Activity
import pl.gawor.android.tayckner.day_tracker.model.Category
import pl.gawor.android.tayckner.day_tracker.repository.ActivityRepositoryDB
import pl.gawor.android.tayckner.day_tracker.repository.CategoryRepositoryDB
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}