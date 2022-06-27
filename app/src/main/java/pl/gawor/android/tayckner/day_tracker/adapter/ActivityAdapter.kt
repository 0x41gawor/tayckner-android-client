package pl.gawor.android.tayckner.day_tracker.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.databinding.DayTrackerDialogUpdateActivityBinding
import pl.gawor.android.tayckner.databinding.DayTrackerItemActivityBinding
import pl.gawor.android.tayckner.day_tracker.model.Activity
import pl.gawor.android.tayckner.day_tracker.model.Category
import pl.gawor.android.tayckner.day_tracker.repository.ActivityRepository
import pl.gawor.android.tayckner.day_tracker.repository.ActivityRepositoryDB
import java.time.LocalDate
import java.util.*

class ActivityAdapter(val context: Context) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {
    inner class ActivityViewHolder(val binding: DayTrackerItemActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardView.setOnClickListener {
                popUpMenu(context, binding.root)
            }
        }

        private val repository = Repository()

        private fun popUpMenu(context: Context, view: View) {
            Log.i(TAG, "ActivityAdapter.popUpMenu()")
            val item = activities[adapterPosition]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.common_menu_item)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.editText -> {
                        val bindingUpdateActivity = DayTrackerDialogUpdateActivityBinding.inflate(LayoutInflater.from(context))

                        val editTextName = bindingUpdateActivity.editTextName
                        val editTextCategoryId = bindingUpdateActivity.editTextCategoryId
                        val editTextStart = bindingUpdateActivity.editTextStart
                        val editTextEnd = bindingUpdateActivity.editTextEnd

                        editTextName.setText(item.name)
                        editTextCategoryId.setText(item.category.id.toString())
                        editTextStart.setText(item.startTime.substring(0,5))
                        editTextEnd.setText(item.endTime.substring(0,5))

                        val dialogUpdateActivity = AlertDialog.Builder(context)

                        dialogUpdateActivity.setView(bindingUpdateActivity.root)

                        dialogUpdateActivity.setPositiveButton("Update") {
                            dialog,_->
                            repository.sendActivitiesUpdateRequest(item.id, editTextName, editTextCategoryId, editTextStart, editTextEnd)
                            Thread.sleep(500)
                            repository.sendActivitiesListRequest()
                            dialog.dismiss()
                        }
                        dialogUpdateActivity.setNegativeButton("Cancel") {
                                dialog,_->
                            dialog.dismiss()
                        }
                        dialogUpdateActivity.create()
                        dialogUpdateActivity.show()
                        true
                    }
                    R.id.delete -> {
                        repository.sendActivitiesDeleteRequest(item.id)
                        Thread.sleep(500)
                        repository.sendActivitiesListRequest()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
        }
    }

    private  val TAG = "TAYCKNER"

    private val diffCallback = object : DiffUtil.ItemCallback<Activity>() {
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var activities: List<Activity>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = DayTrackerItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.binding.apply {
            val activity = activities[position]
            textViewName.text = activity.name
            val start = activity.startTime.substring(0, 5)
            textViewStart.text = start
            val end = activity.endTime.substring(0, 5)
            textViewEnd.text = end
            textViewCategory.text = activity.category.name
            linearLayout.background.current.setTint(Color.parseColor(activity.category.color))
        }
    }

    inner class Repository {
        private val activitiesRepository = ActivityRepositoryDB(context)

        fun sendActivitiesUpdateRequest(id: Int, editTextName: EditText, editTextCategoryId: EditText, editTextStart: EditText, editTextEnd: EditText) {
            Log.i(TAG, "ActivityAdapter.sendActivitiesUpdateRequest()")
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
            if (end.length < 5) end = "0$start"

            val category = Category(categoryId, "", "",  "", null)
            val activity = Activity(9, name , start, end, LocalDate.MIN, 0, 0, category)
            CoroutineScope(Dispatchers.IO).launch {
                activitiesRepository.update(activity, id)
            }
        }

        fun sendActivitiesListRequest() {
            Log.i(TAG, "ActivityAdapter.sendActivitiesListRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                val list :List<Activity> = activitiesRepository.list()
                activities = list
            }
        }

        fun sendActivitiesDeleteRequest(id: Int) {
            Log.i(TAG, "ActivityAdapter.sendActivitiesDeleteRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                activitiesRepository.delete(id)
            }
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }

}