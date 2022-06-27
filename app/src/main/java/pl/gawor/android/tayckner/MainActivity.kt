package pl.gawor.android.tayckner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.gawor.android.tayckner.day_tracker.repository.CategoryRepositoryDB

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val elo = CategoryRepositoryDB(applicationContext)
        elo.list()
    }
}