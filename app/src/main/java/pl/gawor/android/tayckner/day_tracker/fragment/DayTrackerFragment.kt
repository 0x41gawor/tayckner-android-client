package pl.gawor.android.tayckner.day_tracker.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.common.util.SharedPrefManager
import pl.gawor.android.tayckner.common.util.Util
import pl.gawor.android.tayckner.databinding.DayTrackerDialogAddActivityBinding
import pl.gawor.android.tayckner.databinding.DayTrackerFragmentMainBinding
import pl.gawor.android.tayckner.day_tracker.adapter.ActivityAdapter
import pl.gawor.android.tayckner.day_tracker.model.Activity
import pl.gawor.android.tayckner.day_tracker.model.Category
import pl.gawor.android.tayckner.day_tracker.repository.ActivityRepositoryDB
import java.time.LocalDate
import java.util.*

var selectedDate: LocalDate = LocalDate.now()

class DayTrackerFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var binding: DayTrackerFragmentMainBinding

    private lateinit var activityAdapter: ActivityAdapter

    private val repository = Repository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DayTrackerFragmentMainBinding.inflate(layoutInflater)
        setupActivitiesRecyclerView()
        setupDate()

        repository.init(requireContext())
        repository.refreshActivitiesList()

        binding.imageButtonOptions.setOnClickListener {
            optionsMenu(binding.imageButtonOptions)
        }

        binding.buttonPrev.setOnClickListener {
            setSelectedDate(-1)
        }

        binding.buttonNext.setOnClickListener {
            setSelectedDate(1)
        }

        binding.imageButtonDayPlanner.setOnClickListener {
            findNavController().navigate(R.id.action_dayTrackerFragment_to_dayPlannerFragment)
        }

        binding.imageButtonDayTracker.setOnClickListener {

        }

        binding.imageButtonHabitTracker.setOnClickListener {
            findNavController().navigate(R.id.action_dayTrackerFragment_to_habitTrackerFragment)
        }

        binding.imageButtonAdd.setOnClickListener {
            addActivity()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        repository.refreshActivitiesList()
    }

    private fun optionsMenu(view: View) {
        val optionsMenu = PopupMenu(context, view)
        optionsMenu.inflate(R.menu.day_tracker_menu_top_bar)
        optionsMenu.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.logout -> {
                    SharedPrefManager.logout(this)
                    findNavController().navigate(R.id.action_dayTrackerFragment_to_loginFragment)
                    true}
                R.id.categories -> {
                    findNavController().navigate(R.id.action_dayTrackerFragment_to_dayTrackerCategoriesFragment)
                    true
                }
                else -> {true}
            }
        }
        optionsMenu.show()
    }


    private fun setupActivitiesRecyclerView() = binding.recyclerView.apply {
        activityAdapter = ActivityAdapter(context)
        adapter = activityAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupDate() {
        val textViewDate = binding.textViewDate

        val year = selectedDate.year
        val month = selectedDate.month.toString()
        val day = selectedDate.dayOfMonth

        textViewDate.text = "$day ${month.substring(0,3)} $year"
    }

    private fun setSelectedDate(sel: Int) {
        selectedDate = when(sel) {
            0 -> LocalDate.now()
            -1 -> selectedDate.minusDays(1)
            1 -> selectedDate.plusDays(1)
            else -> {LocalDate.now()}
        }
        setupDate()
        repository.refreshActivitiesList()
    }

    private fun addActivity() {
        Log.e(TAG, "DayTrackerFragment.addActivity()")

        val bindingAddActivity = DayTrackerDialogAddActivityBinding.inflate(layoutInflater)

        val editTextName = bindingAddActivity.editTextName
        val editTextCategoryId = bindingAddActivity.editTextCategoryId
        val editTextStart = bindingAddActivity.editTextStart
        val editTextEnd = bindingAddActivity.editTextEnd

        val dialogAddActivity = AlertDialog.Builder(context)

        dialogAddActivity.setView(bindingAddActivity.root)

        dialogAddActivity.setPositiveButton("Add") {
                dialog,_->
                repository.sendActivityCreateRequest(editTextName, editTextCategoryId, editTextStart, editTextEnd)
                Thread.sleep(500)
                repository.refreshActivitiesList()
                dialog.dismiss()
        }
        dialogAddActivity.setNegativeButton("Cancel") {
                dialog,_->
            Toast.makeText(context, "Adding canceled", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialogAddActivity.create()
        dialogAddActivity.show()
        Log.e(TAG, "DayTrackerFragment.addActivity() = void")
    }

    inner class Repository {
        private var activityRepository: ActivityRepositoryDB? = null

        fun init(context: Context) {
            activityRepository = ActivityRepositoryDB(context)
        }

        fun refreshActivitiesList() {
            Log.i(TAG, "DayTrackerFragment.Repository.refreshActivitiesList()")
            lifecycleScope.launchWhenCreated {
                val list: List<Activity> = activityRepository!!.list(selectedDate)
                activityAdapter.activities = list
                return@launchWhenCreated
            }
            Log.i(TAG, "DayTrackerFragment.Repository.refreshActivitiesList() = void")
        }

        fun sendActivityCreateRequest(editTextName: EditText, editTextCategoryId: EditText, editTextStart: EditText, editTextEnd: EditText) {
            Log.i(TAG, "DayTrackerFragment.Repository.sendActivityCreateRequest()")
            val name = editTextName.text.toString()
            val categoryId = editTextCategoryId.text.toString().toInt()

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH) + 1
            val day = c.get(Calendar.DAY_OF_MONTH)

            val date = "$year-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}"

            var start = editTextStart.text.toString()
            if (start.length < 5) start = "0$start"
            var end = editTextEnd.text.toString()
            if (end.length < 5) end = "0$end"

            val category = Category(categoryId, "", "",  "", null)
            val activity = Activity(0, name, start, end, LocalDate.now(), 0, 0, category)

            lifecycleScope.launchWhenCreated {
                activityRepository!!.create(activity)
                return@launchWhenCreated
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DayTrackerFragment().apply {
            }
    }
}