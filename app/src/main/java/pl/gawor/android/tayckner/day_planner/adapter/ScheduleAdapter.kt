package pl.gawor.android.tayckner.day_planner.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.databinding.DayPlannerDialogAddScheduleBinding
import pl.gawor.android.tayckner.databinding.DayPlannerDialogUpdateScheduleBinding
import pl.gawor.android.tayckner.databinding.DayPlannerItemScheduleBinding
import pl.gawor.android.tayckner.day_planner.model.Schedule
import pl.gawor.android.tayckner.day_planner.repository.ScheduleRepository
import java.util.*

class ScheduleAdapter(val context: Context) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    inner class ScheduleViewHolder(val binding: DayPlannerItemScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardView.setOnClickListener {
                popUpMenu(context, binding.root)
            }
        }

        private val repository = Repository()


        private fun popUpMenu(context: Context, view: View) {
            Log.i(TAG, "ScheduleAdapter.popUpMenu()")
            val item = schedules[adapterPosition]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.common_menu_item)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.editText -> {
                        val bindingAddSchedule = DayPlannerDialogUpdateScheduleBinding.inflate(LayoutInflater.from(context))

                        val editTextName = bindingAddSchedule.editTextName
                        val editTextStart = bindingAddSchedule.editTextStart
                        val editTextEnd = bindingAddSchedule.editTextEnd
                        val editTextDuration = bindingAddSchedule.editTextDuration
                        editTextName.setText(item.name)
                        editTextStart.setText(item.startTime.substring(11,16))
                        editTextEnd.setText(item.endTime.substring(11,16))
                        editTextDuration.setText(item.duration.toString())
                        val dialogAddSchedule = AlertDialog.Builder(context)

                        dialogAddSchedule.setView(bindingAddSchedule.root)

                        dialogAddSchedule.setPositiveButton("Update") {
                                dialog,_->
                            repository.sendSchedulesUpdateRequest(item.id, editTextName, editTextStart, editTextEnd, editTextDuration)
                            Thread.sleep(500)
                            repository.sendSchedulesListRequest()
                            dialog.dismiss()
                        }
                        dialogAddSchedule.setNegativeButton("Cancel") {
                                dialog,_->
                            Toast.makeText(context, "Adding canceled", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        dialogAddSchedule.create()
                        dialogAddSchedule.show()
                        true
                    }
                    R.id.delete -> {
                        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
        }
    }

    private  val TAG = "TAYCKNER"

    private val diffCallback = object : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var schedules: List<Schedule>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = DayPlannerItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.binding.apply {
            val schedule = schedules[position]
            textViewName.text = schedule.name
            textViewStart.text = schedule.startTime.substring(11, 16)
            textViewEnd.text = schedule.endTime.substring(11, 16)
            textViewDuration.text = if (schedule.duration == 0)  "" else schedule.duration.toString()
        }
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    inner class Repository {
        private  val scheduleRepository = ScheduleRepository()

        fun sendSchedulesUpdateRequest(id: Int, editTextName: EditText, editTextStart: EditText, editTextEnd: EditText, editTextDuration: EditText) {
            Log.i(TAG, "ScheduleAdapter.Repository.sendSchedulesUpdateRequest()")
            val name = editTextName.text.toString()
            val duration = editTextDuration.text.toString().toInt()

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH) + 1
            val day = c.get(Calendar.DAY_OF_MONTH)

            val date = "$year-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}"

            var start = editTextStart.text.toString()
            var end = editTextEnd.text.toString()

            start = "${date}T${start}:00"
            end = "${date}T${end}:00"

            val schedule = Schedule(duration, end, 0, name, start, null)

            CoroutineScope(Dispatchers.IO).launch {
                scheduleRepository.update(schedule, id)
            }
        }

        fun sendSchedulesListRequest() {
            Log.i(TAG, "ScheduleAdapter.Repository.sendSchedulesListRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                val list :List<Schedule> = scheduleRepository.list()
                schedules = list
            }
        }

        fun sendSchedulesDeleteRequest(id: Int) {
            Log.i(TAG, "ScheduleAdapter.Repository.sendSchedulesDeleteRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                scheduleRepository.delete(id)
            }
        }

    }
}